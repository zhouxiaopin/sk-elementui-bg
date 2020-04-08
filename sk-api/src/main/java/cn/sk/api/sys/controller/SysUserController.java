package cn.sk.api.sys.controller;

import cn.sk.api.base.controller.BaseController;
import cn.sk.api.sys.service.IFileService;
import cn.sk.api.sys.common.SkLog;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.common.TokenCache;
import cn.sk.api.sys.pojo.SysUser;
import cn.sk.api.sys.pojo.SysUserQueryVo;
import cn.sk.api.sys.service.ISysUserService;
import cn.sk.api.sys.utils.AppContext;
import cn.sk.api.sys.utils.JwtUtil;
import cn.sk.api.sys.utils.ShiroUtils;
import cn.sk.common.common.ResponseCode;
import cn.sk.common.common.ServerResponse;
import cn.sk.common.utils.DateUtils;
import cn.sk.poi.utils.ExportExcelUtil;
import cn.sk.poi.utils.ImportExcelUtil;
import cn.sk.poi.vo.BatchImportVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 系统用户 Controller
 */
@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController extends BaseController<SysUser, SysUserQueryVo> {

    private static final String UPDATE_PASSWORD_OPRT = "updatePassword";
    private static final String LOGIN_OPRT = "login";

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IFileService fileService;


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
//        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUser::getUserName,userName);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
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

    //导出数据
    @PostMapping(value = "/export",produces = "application/octet-stream;charset=UTF-8")
    public void export(@RequestBody SysUserQueryVo sysUserQueryVo,HttpServletResponse response){

        List<SysUser> sysUsers = this.queryAllByCondition(sysUserQueryVo).getData();

        ExportExcelUtil<SysUser> exportExcelUtil = new ExportExcelUtil<>();
        ExportExcelUtil.ExportParam<SysUser> params = ExportExcelUtil.ExportParam.<SysUser>builder()
                .data(sysUsers).response(response).fileName("员工列表")
                .tempFileIn(AppContext.getInputStreamByFilePath("static/file/excel/emp-template.xls")).build();
//                .tempPath("emp-template.xls").build();

        exportExcelUtil.exportExcel03(params);
    }

    //批量导入
    @PostMapping(value = "/batchImport")
    public ServerResponse batchImport(@RequestParam("file") MultipartFile[] files) throws IOException {
        ImportExcelUtil<SysUser> importExcelUtil = new ImportExcelUtil<>();
        ImportExcelUtil.ImportParam<SysUser> params;
        List<SysUser> list = Lists.newArrayList();

        List<Map<String,Object>> failList = Lists.newArrayList();

        for(int i = 0,len = files.length; i < len; i++) {
            params = ImportExcelUtil.ImportParam.<SysUser>builder()
                    .is(files[i].getInputStream())
                    .fileName(files[i].getOriginalFilename())
                    .hasPic(true)
                    .clazz(SysUser.class).build();

            List<SysUser> itemList = importExcelUtil.importExcel(params);
            if(i == 0) {
                Map<String,Object> failItem = Maps.newHashMap();
                failItem.put("userName",itemList.get(0).getUserName());
                failItem.put("failMsg","未知错误");
                failList.add(failItem);

                PictureData pic = itemList.get(0).getHeadImg();
                // 获取图片格式
                String fileExtensionName = pic.suggestFileExtension();
                String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
                byte[] data = pic.getData();
                String fileUploadPath = this.getClass().getClassLoader().getResource("static/").getPath()+"upload/";
                File targetFile = new File(fileUploadPath,uploadFileName);
                FileUtils.forceMkdirParent(targetFile);
                FileUtils.writeByteArrayToFile(targetFile,data);
                log.info("开始上传文件,路径:{"+targetFile.getPath()+"}");
                log.info("开始上传文件,新文件名:{"+uploadFileName+"}");
            }
            list.addAll(itemList);
        }
        BatchImportVo<SysUser> batchImportVo = BatchImportVo.<SysUser>builder()
                .totalNum(list.size()).failList(failList).build();

        return ServerResponse.createBySuccess(batchImportVo);
    }

    //下载导入模板
    @GetMapping(value = "/downImportTemp",produces = "application/octet-stream;charset=UTF-8")
    public void downImportTemp(HttpServletResponse response){
        authorityValidate(DOWN_IMPORT_TEMP);
        fileService.downTemplateFile(response,"excel/emp-template.xls");
    }

    //上传图片
    @PostMapping(value = "/uploadImg")
    public ServerResponse uploadImg(@RequestParam("file") MultipartFile[] files){

        return fileService.imgUpload(files,"");
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

    //参数检验
    @Override
    protected ServerResponse<SysUser> paramValidate(String oprt, SysUser sysUser) {
        ServerResponse<List<SysUser>> serverResponse;
        SysUserQueryVo sysUserQueryVo;
        switch (oprt) {
            case ADD_OPRT://添加
                if (!StringUtils.equals(sysUser.getPassword(), sysUser.getPassword2())) {
                    return ServerResponse.createByErrorMessage("两次密码输入不一致");
                }

                //判断字用户名是否存在

                sysUserQueryVo = SysUserQueryVo.newInstance();
                sysUserQueryVo.getCdtCustom().setUserName(sysUser.getUserName());
                sysUserQueryVo.getIsNoLike().put("userName",true);
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

    //权限前缀
    @Override
    protected String getPermisPrefix() {
        return SysConst.ShiroPermis.PermisPrefix.SYSUSER;
    }

    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        super.authorityValidate(oprt);
        switch (oprt) {
            case UPDATE_PASSWORD_OPRT://修改密码
                SecurityUtils.getSubject().checkPermission(getPermisPrefix()+UPDATE_PASSWORD_OPRT);
                break;
        }
    }
}
