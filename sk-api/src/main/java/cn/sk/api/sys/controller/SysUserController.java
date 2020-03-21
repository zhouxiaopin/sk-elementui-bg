package cn.sk.api.sys.controller;

import cn.sk.api.base.controller.BaseController;
import cn.sk.api.sys.common.*;
import cn.sk.api.sys.pojo.SysUser;
import cn.sk.api.sys.pojo.SysUserQueryVo;
import cn.sk.api.sys.service.ISysUserService;
import cn.sk.api.sys.utils.DateUtils;
import cn.sk.api.sys.utils.JwtUtil;
import cn.sk.api.sys.utils.ShiroUtils;
import cn.sk.poi.utils.ExportExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户 Controller
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController<SysUser, SysUserQueryVo> {

    private static final String UPDATE_PASSWORD_OPRT = "updatePassword";
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    private static final String LOGIN_OPRT = "login";

    @Autowired
    private ISysUserService sysUserService;


    //进入登录页面
//    @GetMapping(value = "/initLogin")
//    public ModelAndView initLogin(ModelAndView model){
//        model.addObject(OPRT_KEY, LOGIN_OPRT);
//        model.setViewName(page(LOGIN_OPRT));
//        return model;
//    }

    @SkLog(value ="登录系统", saveParams=false)
    @PostMapping(value = "/login")
    public ServerResponse login(@RequestBody SysUser su){
        String userName = su.getUserName();
        String password = su.getPassword();

//        SysUserQueryVo sysUserQueryVo = SysUserQueryVo.newInstance();
//        sysUserQueryVo.getCdtCustom().setUserName(userName);
//        sysUserQueryVo.getIsNoLike().put("userName",true);

//        sysUserService.getObj();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName,userName);
        SysUser sysUser = sysUserService.getOne(queryWrapper);


        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if(null == sysUser) {
            return ServerResponse.createByError(ResponseCode.LOGIN_NO_EXIST);
        }

        String salt = sysUser.getSalt();
        if(!sysUser.getPassword().equals(ShiroUtils.getMd5Pwd(salt,new String(password)))) {
            return ServerResponse.createByError(ResponseCode.LOGIN_PWD_FAIL);
        }

        if(StringUtils.equals(SysConst.RecordStatus.DISABLE,sysUser.getRecordStatus())) {
            return ServerResponse.createByError(ResponseCode.LOGIN_NO_USE);
        }
//                 生成token
        String token = JwtUtil.sign(userName, sysUser.getPassword());
        TokenCache.setKey(SysConst.PREFIX_USER_TOKEN+token,token);
//                 设置token缓存有效时间
//                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
//                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);
        Map<String,Object> data = Maps.newHashMap();
        data.put("token",token);
        data.put("expiresTime", DateUtils.addMinuteTime(new Date(),30));
        data.put("user",sysUser);
        return ServerResponse.createBySuccess(ResponseCode.LOGIN_SUCCESS.getMsg(),data);

    }

    @SkLog(value ="退出系统", saveParams=false)
    @GetMapping(value = "/logout")
    public ServerResponse logout(HttpServletRequest request, HttpServletResponse response){
        //用户退出逻辑
        String token = request.getHeader(SysConst.X_ACCESS_TOKEN);
        if(StringUtils.isEmpty(token)) {
            return ServerResponse.createByError(ResponseCode.LOGOUT_FAIL);
        }
        String userName = JwtUtil.getUsername(token);

        SysUserQueryVo sysUserQueryVo = SysUserQueryVo.newInstance();
        sysUserQueryVo.getCdtCustom().setUserName(userName);
        sysUserQueryVo.getIsNoLike().put("userName",true);
//        SysUserQueryVo sysUserQueryVo = new SysUserQueryVo();
//        SysUserCustom condition = new SysUserCustom();
//        condition.setUserName(userName);
//        sysUserQueryVo.getIsNoLike().put("userName",true);
//        sysUserQueryVo.setSysUserCustom(condition);


        List<SysUser> sysUsers = sysUserService.queryObjs(sysUserQueryVo).getData();


        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if(CollectionUtils.isEmpty(sysUsers)) {
            return ServerResponse.createByError(ResponseCode.TOKEN_LOSE_EFFICACY);
        }


        //清空用户登录Token缓存
        TokenCache.invalidate(SysConst.PREFIX_USER_TOKEN+token);
//        redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);

        //调用shiro的logout
        SecurityUtils.getSubject().logout();
        return ServerResponse.createBySuccessMessage(ResponseCode.LOGOUT_SUCCESS.getMsg());
    }

    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysUser> updateRecordStatus(@RequestBody SysUser sysUser) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysUser.getRecordStatus();

        ServerResponse<SysUser> serverResponse = sysUserService.update(sysUser);
        if (StringUtils.equals(rs, SysConst.RecordStatus.ABLE)) {
            if (serverResponse.isSuccess()) {
                serverResponse.setMsg("启用成功");
            }else{
                serverResponse.setMsg("启用失败");
            }
            logUtil.writLog(this.getClass(),"updateRecordStatus","系统用户启用");
        } else if (StringUtils.equals(rs, SysConst.RecordStatus.DISABLE)) {
            if (serverResponse.isSuccess()) {
                serverResponse.setMsg("禁用成功");
            }else{
                serverResponse.setMsg("禁用失败");
            }
            logUtil.writLog(this.getClass(),"updateRecordStatus","系统用户禁用");
        }

        return serverResponse;
    }

    //进入修改密码页面
//    @GetMapping(value = "/initUpdatePassword")
//    public ModelAndView initUpdatePassword(ModelAndView model, SysUserCustom sysUserCustom) throws Exception {
//        //权限校验
//        authorityValidate(UPDATE_PASSWORD_OPRT);
//        model.addObject(OPRT_KEY, UPDATE_PASSWORD_OPRT);
//        try {
//            init(model, sysUserCustom);
//        } catch (Exception e) {
//            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
//        }
//        model.setViewName(page(UPDATE_PASSWORD_OPRT));
//        return model;
//    }

    //修改密码
    @SkLog(value ="修改密码", saveParams=false)
    @PostMapping(value = "updatePassword")
    public ServerResponse<SysUser> updatePassword(@RequestBody SysUser sysUser) {
        //权限校验
        authorityValidate(UPDATE_PASSWORD_OPRT);
        //参数检验
        ServerResponse sr = paramValidate(UPDATE_PASSWORD_OPRT, sysUser);
        if (!sr.isSuccess()) {
            return sr;
        }

        //业务逻辑
        SysUser oldObj = getObj(sysUser);
        //判断盐值是否存在
        String salt = oldObj.getSalt();
        if (StringUtils.isEmpty(salt)) {
            salt = ShiroUtils.DEFALT_SALT;
            sysUser.setSalt(salt);
        }
        sysUser.setPassword(ShiroUtils.getMd5Pwd(salt, sysUser.getPassword()));

        return sysUserService.update(sysUser);
    }



    /****************************以下是重新父类的方法*****************************/
    //修改之前
    @Override
    protected ServerResponse<SysUser> updateBefore(SysUser oldObj, SysUser sysUser) {
        //判断是否有修改密码
        if (!StringUtils.equals(sysUser.getPassword(), sysUser.getPassword2())) {
            //判断盐值是否存在
            String salt = oldObj.getSalt();
            if (StringUtils.isEmpty(salt)) {
                salt = ShiroUtils.DEFALT_SALT;
                sysUser.setSalt(salt);
            }
            sysUser.setPassword(ShiroUtils.getMd5Pwd(salt, sysUser.getPassword()));
        }
        return super.updateBefore(oldObj, sysUser);
    }

    //根据oprt返回对应的页面
//    @Override
//    protected String getPage(String oprt) {
//        String prefix = "sys/sysUser/";
//        if (oprt.equals(LOGIN_OPRT)) {
//            return "login";
//        }
//        if (oprt.equals(QUERY_OPRT)) {
//            return prefix + "sysUserQuery";
//        }
//        if (oprt.equals(UPDATE_OPRT)) {
//            return prefix + "sysUser";
//        }
//        if (oprt.equals(ADD_OPRT)) {
//            return prefix + "sysUser";
//        }
//        if (oprt.equals(UPDATE_PASSWORD_OPRT)) {
//            return prefix + "sysUser";
//        }
//        return super.getPage(oprt);
//    }

    @GetMapping(value = "/export")
    public void export(HttpServletResponse response){

        List<SysUser> sysUsers = this.queryAllByCondition(null).getData();

        ExportExcelUtil<SysUser> exportExcelUtil = new ExportExcelUtil<>();
        ExportExcelUtil.ExportParam<SysUser> params = ExportExcelUtil.ExportParam.<SysUser>builder()
                .data(sysUsers).response(response).fileName("员工列表")
                .tempPath("emp-template.xls").build();

        exportExcelUtil.exportExcel03(params);
    }

    //参数检验
    @Override
    protected ServerResponse<SysUser> paramValidate(String oprt, SysUser sysUser) {
        ServerResponse<List<SysUser>> serverResponse;
        SysUserQueryVo sysUserQueryVo;
        SysUser condition;
        switch (oprt) {
            case ADD_OPRT://添加
                if (!StringUtils.equals(sysUser.getPassword(), sysUser.getPassword2())) {
                    return ServerResponse.createByErrorMessage("两次密码输入不一致");
                }

                //判断字用户名是否存在

                sysUserQueryVo = SysUserQueryVo.newInstance();
                sysUserQueryVo.getCdtCustom().setUserName(sysUser.getUserName());
                sysUserQueryVo.getIsNoLike().put("userName",true);

//                sysUserQueryVo = new SysUserQueryVo();
//                condition = new SysUserCustom();
//
//                sysUserQueryVo.getIsNoLike().put("userName",true);
//
//                condition.setUserName(sysUserCustom.getUserName());
//
//                sysUserQueryVo.setSysUserCustom(condition);
                serverResponse = this.queryAllByCondition(sysUserQueryVo);
                if(!CollectionUtils.isEmpty(serverResponse.getData())){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }

                break;
            case UPDATE_OPRT://修改
                //判断字用户名是否存在
                sysUserQueryVo = SysUserQueryVo.newInstance();
                sysUserQueryVo.getCdtCustom().setUserName(sysUser.getUserName());
                sysUserQueryVo.getIsNoLike().put("userName",true);
//                sysUserQueryVo = new SysUserQueryVo();
//                condition = new SysUserCustom();
//
//                sysUserQueryVo.getIsNoLike().put("userName",true);
//
//                condition.setUserName(sysUserCustom.getUserName());
//
//                sysUserQueryVo.setSysUserCustom(condition);
                serverResponse = this.queryAllByCondition(sysUserQueryVo);
                List<SysUser> sysUserCustoms = serverResponse.getData();
                if(!CollectionUtils.isEmpty(sysUserCustoms)){
                    for (int i = 0, len = sysUserCustoms.size(); i < len; i++){
                        if(sysUser.getUserId() != sysUserCustoms.get(i).getUserId()) {
                            return ServerResponse.createByErrorMessage("用户名已存在");
                        }
                    }
                }

                break;
            case UPDATE_PASSWORD_OPRT://修改密码
                if (StringUtils.isEmpty(sysUser.getPassword()) || StringUtils.isEmpty(sysUser.getPassword2())) {
                    return ServerResponse.createByErrorMessage("密码不能为空");
                }
                if (!StringUtils.equals(sysUser.getPassword(), sysUser.getPassword2())) {
                    return ServerResponse.createByErrorMessage("两次密码输入不一致");
                }

                break;
        }
        return super.paramValidate(oprt, sysUser);
    }

    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
            case ADD_OPRT://添加
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.ADD);
                break;
            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.UPDATE_RECORDSTATUS);
                break;
            case UPDATE_OPRT://修改
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.UPDATE);
                break;
            case UPDATE_PASSWORD_OPRT://修改密码
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.UPDATE_PSD);
                break;
            case DEL_OPRT://删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.DEL);
                break;
            case REAL_DEL_OPRT://硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.REAL_DEL);
                break;
            case BATCH_DEL_OPRT://批量删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.BATCH_DEL);
                break;
            case BATCH_REAL_DEL_OPRT://批量硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.BATCH_REAL_DEL);
                break;
        }
    }
}