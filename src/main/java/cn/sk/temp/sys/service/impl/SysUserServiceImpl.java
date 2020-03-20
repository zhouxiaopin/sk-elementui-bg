package cn.sk.temp.sys.service.impl;

import cn.sk.temp.base.service.impl.BaseServiceImpl;
import cn.sk.temp.sys.common.CustomException;
import cn.sk.temp.sys.common.ResponseCode;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.mapper.SysRoleMapper;
import cn.sk.temp.sys.mapper.SysUserMapper;
import cn.sk.temp.sys.mapper.SysUserRoleMapper;
import cn.sk.temp.sys.pojo.SysUser;
import cn.sk.temp.sys.pojo.SysUserQueryVo;
import cn.sk.temp.sys.pojo.SysUserRole;
import cn.sk.temp.sys.service.ISysUserService;
import cn.sk.temp.sys.utils.ShiroUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统用户业务逻辑接口实现类
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, SysUserQueryVo,SysUserMapper> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    protected ServerResponse<SysUser> insertBefore(SysUser sysUser) {
        //判断盐值是否存在
        String salt = sysUser.getSalt();
        if(StringUtils.isEmpty(salt)) {
            salt = ShiroUtils.DEFALT_SALT;
            sysUser.setSalt(salt);
        }
        sysUser.setPassword(ShiroUtils.getMd5Pwd(salt,sysUser.getPassword()));
        sysUser.setRecordStatus(SysConst.RecordStatus.DISABLE);
//        SysUser.setCreateTime(new Date());
        return super.insertBefore(sysUser);
    }

    @Override
    protected ServerResponse<SysUser> insertAfter(SysUser sysUser) {
        //判断是否有设置角色
        String[] roleIds = StringUtils.split(sysUser.getRoleIds(),",");
        if(!ArrayUtils.isEmpty(roleIds)) {
            List<SysUserRole> sysUserRoles = Lists.newArrayList();
            for(int i = 0,len = roleIds.length; i < len; i++) {
                SysUserRole sysUserRole = new SysUserRole(null,sysUser.getUserId(),
                        Integer.valueOf(roleIds[i]));
                sysUserRoles.add(sysUserRole);
            }
            int num = sysUserRoleMapper.batchInsert(sysUserRoles);
            if(num < 1) {
                throw new CustomException(ResponseCode.ADD_FAIL);
            }
        }
        return super.insertAfter(sysUser);
    }

    @Override
    public ServerResponse<SysUser> queryObj(SysUser entity) {
        ServerResponse<SysUser> serverResponse = super.queryObj(entity);
        //查询用户角色
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByUserId(entity.getUserId());
        StringBuilder roleIds = new StringBuilder();
        for(int i = 0,len = sysUserRoles.size(); i < len; i++) {
            roleIds.append(sysUserRoles.get(i).getRoleId()).append(",");
        }
        if(roleIds.length()>1) {
            roleIds.deleteCharAt(roleIds.length()-1);
        }
        serverResponse.getData().setRoleIds(roleIds.toString());
        return serverResponse;
    }

    @Override
    protected ServerResponse<SysUser> updateBefore(SysUser SysUser) {
        //判断是否有修改角色
        if(StringUtils.equals(SysUser.getRoleIds(),SysUser.getOldRoleIds())) {
            return super.updateBefore(SysUser);
        }

        //查询系统用户之前是有拥有角色
        Integer userId = SysUser.getUserId();
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByUserId(userId);
        if(!CollectionUtils.isEmpty(sysUserRoles)) {
            //清除原来的角色
            int num = sysUserRoleMapper.realDeleteInUserId(userId);
            if(num < 1) {
                throw new CustomException(ResponseCode.MDF_FAIL);
            }
        }


        //判断是否有设置角色
        String[] roleIds = StringUtils.split(SysUser.getRoleIds(),",");
        if(!ArrayUtils.isEmpty(roleIds)) {
            sysUserRoles = Lists.newArrayList();
            for(int i = 0,len = roleIds.length; i < len; i++) {
                SysUserRole sysUserRole = new SysUserRole(null,SysUser.getUserId(),
                        Integer.valueOf(roleIds[i]));
                sysUserRoles.add(sysUserRole);
            }
            int num = sysUserRoleMapper.batchInsert(sysUserRoles);
            if(num < 1) {
                throw new CustomException(ResponseCode.MDF_FAIL);
            }
        }
        return super.updateBefore(SysUser);
    }

    @Override
    public ServerResponse<List<SysUser>> queryObjs(SysUserQueryVo entityQueryVo) {

        List<SysUser> list = skBaseMapper.selectListByQueryVo(entityQueryVo);

        Map<String,Object> params = Maps.newHashMap();
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        StringBuilder roleNames = new StringBuilder();
        for(int i = 0,len = list.size(); i < len; i++) {
            SysUser SysUser = list.get(i);

            params.put("userId",SysUser.getUserId());
            //根据用户id查找权限
            List<Map<String,Object>> sysRoles = sysRoleMapper.selectListByUserId(params);
            roleNames.delete(0,roleNames.length());
            for(int j = 0,length = sysRoles.size(); j < length; j++) {
                roleNames.append(sysRoles.get(j).get("roleName")).append(",");
            }
            if(roleNames.length() >1 )roleNames.deleteCharAt(roleNames.length()-1);
            list.get(i).setRoleName(roleNames.toString());
        }

        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,list);
    }

    @Override
    protected ServerResponse<SysUser> deleteInIdsAfter(String[] ids) {
        deleteAfter(ids);
        return super.deleteInIdsAfter(ids);
    }

    @Override
    protected ServerResponse<SysUser> realDeleteInIdsAfter(String[] ids) {
        deleteAfter(ids);
        return super.realDeleteInIdsAfter(ids);
    }

    private void deleteAfter(String[] ids){
        for(int i = 0,len = ids.length; i < len; i++) {
            sysUserRoleMapper.realDeleteInUserId(Integer.valueOf(ids[i]));
        }
    }

}
