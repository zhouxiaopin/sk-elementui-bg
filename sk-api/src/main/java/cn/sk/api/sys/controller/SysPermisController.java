package cn.sk.api.sys.controller;

import cn.sk.api.base.controller.BaseController;
import cn.sk.api.sys.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.pojo.SysPermis;
import cn.sk.api.sys.pojo.SysPermisQueryVo;
import cn.sk.api.sys.service.ISysPermisService;
import cn.sk.api.sys.utils.SysUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统权限 Controller
 */
@RestController
@RequestMapping("/sysPermis")
@RequiresAuthentication
public class SysPermisController extends BaseController<SysPermis, SysPermisQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Autowired
    private ISysPermisService sysPermisService;



    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysPermis> updateRecordStatus(@RequestBody SysPermis sysPermis) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysPermis.getRecordStatus();
        ServerResponse<SysPermis> serverResponse = sysPermisService.update(sysPermis);
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

    //获取树形
    @PostMapping(value = "querySysPermisTree")
    public ServerResponse<List<Map<String,Object>>> querySysPermisTree(SysPermisQueryVo sysPermisQueryVo) {
        return sysPermisService.querySysPermisTree(sysPermisQueryVo);
    }

    //获取权限
    @PostMapping(value = "getPermis")
    public ServerResponse getPermis() {
        return sysPermisService.getPermis();
    }



    /****************************以下是重新父类的方法*****************************/

    //参数检验
    @Override
    protected ServerResponse<SysPermis> paramValidate(String oprt, SysPermis sysPermis) {
        switch (oprt) {
            case ADD_OPRT://添加
//                if (StringUtils.isEmpty(sysRoleCustom.getRoleFlag())||StringUtils.isEmpty(sysRoleCustom.getRoleName())) {
//                    return ServerResponse.createByParamError();
//                }

                if(ObjectUtils.isEmpty(sysPermis.getParentId())) {
                    sysPermis.setParentId(SysConst.Permis.DEFAULT_PARENTID);
                }
                sysPermis.setOptId(SysUtils.getUserId());
                //默认可用
                sysPermis.setRecordStatus(SysConst.RecordStatus.ABLE);
                break;
        }
        return super.paramValidate(oprt, sysPermis);
    }

    //权限前缀
    @Override
    protected String getPermisPrefix() {
        return SysConst.ShiroPermis.PermisPrefix.SYSPERMIS;
    }

}
