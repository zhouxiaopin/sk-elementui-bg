package cn.sk.temp.sys.controller;

import cn.sk.temp.base.controller.BaseController;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.pojo.SysSqlConf;
import cn.sk.temp.sys.pojo.SysSqlConfQueryVo;
import cn.sk.temp.sys.service.ISysSqlConfService;
import cn.sk.temp.sys.utils.SysUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统sql语句配置 Controller
 */
@RestController
@RequestMapping("/sysSqlConf")
public class SysSqlConfController extends BaseController<SysSqlConf, SysSqlConfQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Autowired
    private ISysSqlConfService sysSqlConfService;

    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysSqlConf> updateRecordStatus(@RequestBody SysSqlConf sysSqlConf) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysSqlConf.getRecordStatus();
        ServerResponse<SysSqlConf> serverResponse = sysSqlConfService.update(sysSqlConf);
        if (serverResponse.isSuccess()) {
            if (StringUtils.equals(rs, SysConst.RecordStatus.ABLE)) {
                serverResponse.setMsg("启用成功");
            } else if (StringUtils.equals(rs, SysConst.RecordStatus.DISABLE)) {
                serverResponse.setMsg("禁用成功");
            }
        } else {
            if (StringUtils.equals(rs, SysConst.RecordStatus.ABLE)) {
                serverResponse.setMsg("启用失败");
            } else if (StringUtils.equals(rs, SysConst.RecordStatus.DISABLE)) {
                serverResponse.setMsg("禁用失败");
            }
        }
        return serverResponse;
    }
    //根据sql配置编码查询真正的数据
    @PostMapping(value = "queryRealByScCode")
    public ServerResponse<SysSqlConf> queryRealByScCode(@RequestBody SysSqlConf sysSqlConf) {
        //权限校验
//        authorityValidate(UPDATE_RECORDSTATUS_OPRT);
            String scCode = sysSqlConf.getScCode();
            if(StringUtils.isBlank(scCode)) {
                return ServerResponse.createByParamError();
            }
        return sysSqlConfService.queryRealByScCode(scCode);
    }


    /****************************以下是重新父类的方法*****************************/

    //根据oprt返回对应的页面
//    @Override
//    protected String getPage(String oprt) {
//        String prefix = "sys/sysSqlConf/";
//        if (oprt.equals(QUERY_OPRT)) {
//            return prefix + "sysSqlConfQuery";
//        }
//        if (oprt.equals(UPDATE_OPRT)) {
//            return prefix + "sysSqlConf";
//        }
//        if (oprt.equals(ADD_OPRT)) {
//            return prefix + "sysSqlConf";
//        }
//        return super.getPage(oprt);
//    }
    //参数检验
    @Override
    protected ServerResponse<SysSqlConf> paramValidate(String oprt, SysSqlConf sysSqlConf) {
        if(StringUtils.equals(oprt,ADD_OPRT)) {//添加
            //判断语句编码是否存在
            SysSqlConfQueryVo sysSqlConfQueryVo = SysSqlConfQueryVo.newInstance();
            sysSqlConfQueryVo.getCdtCustom().setScCode(sysSqlConf.getScCode());
            sysSqlConfQueryVo.getIsNoLike().put("dictType",true);


            ServerResponse<List<SysSqlConf>> serverResponse = this.queryAllByCondition(sysSqlConfQueryVo);
            if(!CollectionUtils.isEmpty(serverResponse.getData())){
                return ServerResponse.createByErrorMessage("语句编码已存在");
            }
            sysSqlConf.setOptId(SysUtils.getUserId());
            //默认可用
            sysSqlConf.setRecordStatus(SysConst.RecordStatus.ABLE);
        }
        if(StringUtils.equals(oprt,UPDATE_OPRT)) {//修改
            //判断语句编码是否存在
            SysSqlConfQueryVo sysSqlConfQueryVo = SysSqlConfQueryVo.newInstance();
            sysSqlConfQueryVo.getCdtCustom().setScCode(sysSqlConf.getScCode());
            sysSqlConfQueryVo.getIsNoLike().put("dictType",true);



            ServerResponse<List<SysSqlConf>> serverResponse = this.queryAllByCondition(sysSqlConfQueryVo);
            List<SysSqlConf> sysSqlConfCustoms = serverResponse.getData();
            if(!CollectionUtils.isEmpty(sysSqlConfCustoms)){
                for (int i = 0, len = sysSqlConfCustoms.size(); i < len; i++){
                    if(sysSqlConf.getScId() != sysSqlConfCustoms.get(i).getScId()) {
                        return ServerResponse.createByErrorMessage("语句编码已存在");
                    }
                }
            }

            //判断字典编码是否存在
//            SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
//            SysDictCustom condition = new SysDictCustom();
//
//            sysDictQueryVo.getIsNoLike().put("dictType",true);
//
//            condition.setDictType(sysDictCustom.getDictType());
//            condition.setDictCode(sysDictCustom.getDictCode());
//
//            sysDictQueryVo.setSysDictCustom(condition);
//            ServerResponse<List<SysDictCustom>> serverResponse = this.queryAllByCondition(sysDictQueryVo);
//            List<SysDictCustom> sysDictCustoms = serverResponse.getData();
//            if(!CollectionUtils.isEmpty(sysDictCustoms)){
//                for (int i = 0, len = sysDictCustoms.size(); i < len; i++){
//                    if(sysDictCustom.getDictId() != sysDictCustoms.get(i).getDictId()) {
//                        return ServerResponse.createByErrorMessage("字典编码已存在");
//                    }
//                }
//
//            }
        }

        return super.paramValidate(oprt, sysSqlConf);
    }
    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
            case ADD_OPRT://添加
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.ADD);
                break;
            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.UPDATE_RECORDSTATUS);
                break;
            case UPDATE_OPRT://修改
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.UPDATE);
                break;
            case DEL_OPRT://删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.DEL);
                break;
            case REAL_DEL_OPRT://硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.REAL_DEL);
                break;
            case BATCH_DEL_OPRT://批量删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.BATCH_DEL);
                break;
            case BATCH_REAL_DEL_OPRT://批量硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysSqlConf.BATCH_REAL_DEL);
                break;
        }
    }
}
