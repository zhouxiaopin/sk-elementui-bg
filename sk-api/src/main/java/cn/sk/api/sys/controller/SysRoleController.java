package cn.sk.api.sys.controller;

import cn.sk.api.base.controller.BaseController;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.pojo.SysRole;
import cn.sk.api.sys.pojo.SysRoleQueryVo;
import cn.sk.api.sys.service.ISysRoleService;
import cn.sk.api.sys.utils.SysUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色 Controller
 */
@RestController
@RequestMapping("/sysRole")
//@RequiresAuthentication
public class SysRoleController extends BaseController<SysRole, SysRoleQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Autowired
    private ISysRoleService sysRoleService;


    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysRole> updateRecordStatus(@RequestBody SysRole sysRole) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysRole.getRecordStatus();
        ServerResponse<SysRole> serverResponse = sysRoleService.update(sysRole);
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


    /****************************以下是重新父类的方法*****************************/

    //参数检验
    @Override
    protected ServerResponse<SysRole> paramValidate(String oprt, SysRole sysRole) {
        SysRoleQueryVo sysRoleQueryVo;
        SysRole condition;
        ServerResponse<List<SysRole>> serverResponse;
        switch (oprt) {
            case ADD_OPRT://添加
                if (StringUtils.isEmpty(sysRole.getRoleFlag())||StringUtils.isEmpty(sysRole.getRoleName())) {
                    return ServerResponse.createByParamError();
                }

                //判断角色标识是否存在

                sysRoleQueryVo = SysRoleQueryVo.newInstance();
                sysRoleQueryVo.getCdtCustom().setRoleFlag(sysRole.getRoleFlag());
                sysRoleQueryVo.getIsNoLike().put("roleFlag",true);


                serverResponse = this.queryAllByCondition(sysRoleQueryVo);
                if(!CollectionUtils.isEmpty(serverResponse.getData())){
                    return ServerResponse.createByErrorMessage("角色标识已存在");
                }

                sysRole.setOptId(SysUtils.getUserId());
                //默认可用
                sysRole.setRecordStatus(SysConst.RecordStatus.ABLE);
                break;
            case UPDATE_OPRT://修改
                //判断角色标识是否存在
                sysRoleQueryVo = SysRoleQueryVo.newInstance();
                sysRoleQueryVo.getCdtCustom().setRoleFlag(sysRole.getRoleFlag());
                sysRoleQueryVo.getIsNoLike().put("roleFlag",true);

                serverResponse = this.queryAllByCondition(sysRoleQueryVo);
                List<SysRole> sysRoles = serverResponse.getData();
                if(!CollectionUtils.isEmpty(sysRoles)){
                    for (int i = 0, len = sysRoles.size(); i < len; i++){
                        if(sysRole.getRoleId() != sysRoles.get(i).getRoleId()) {
                            return ServerResponse.createByErrorMessage("角色标识已存在");
                        }
                    }
                }
                break;
        }
        return super.paramValidate(oprt, sysRole);
    }

    //权限前缀
    @Override
    protected String getPermisPrefix() {
        return SysConst.ShiroPermis.PermisPrefix.SYSROLE;
    }
}
