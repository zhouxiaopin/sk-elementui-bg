package cn.sk.api.sys.service.impl;

import cn.sk.api.base.service.impl.BaseServiceImpl;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.mapper.SysPermisMapper;
import cn.sk.api.sys.mapper.SysRoleMapper;
import cn.sk.api.sys.pojo.*;
import cn.sk.api.sys.service.ISysPermisService;
import cn.sk.api.sys.utils.SysUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统权限业务逻辑接口实现类
 */
@Service
public class SysPermisServiceImpl extends BaseServiceImpl<SysPermis, SysPermisQueryVo,SysPermisMapper> implements ISysPermisService {
    @Autowired
    private SysPermisMapper sysPermisMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;


    @Override
    public ServerResponse<List<Map<String, Object>>> querySysPermisTree(SysPermisQueryVo sysPermisQueryVo) {
        List<SysPermis> sysPermiss = sysPermisMapper.selectListByQueryVo(sysPermisQueryVo);
        List<Map<String, Object>> data = Lists.newArrayList();
        for (int i = 0,len = sysPermiss.size(); i < len; i++){
            SysPermis sysPermis = sysPermiss.get(i);
//            {id:6, pId:0, name:"福建省", open:true, nocheck:true},
//            { id:1, pId:0, name:"一级分类", open:true},
            Map<String,Object> item = Maps.newHashMap();
            item.put("id",sysPermis.getPerId());
            item.put("pId",sysPermis.getParentId());
            item.put("name",sysPermis.getPerName());
            item.put("level",sysPermis.getPerLevel());
            item.put("open",true);
            data.add(item);
//
        }
        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse getPermis() {
        Map<String,Object> params = Maps.newHashMap();
        params.put("userId", SysUtils.getUserId());
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        List<Map<String,Object>> sysRoleCustoms = sysRoleMapper.selectListByUserId(params);
        //2. 利用登录的用户的信息来用户当前用户的角色或权限(可能需要查询数据库)
//        Set<String> roles = new HashSet<>();
        Set<Integer> roleIds = Sets.newHashSet();
        Set<String> permissions = new HashSet<>();
        Map<String,Object> item;
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            for (int i = 0, len = sysRoleCustoms.size(); i < len; i++){
                item = sysRoleCustoms.get(i);
//                roles.add(item.get("roleFlag").toString());
                roleIds.add(Integer.valueOf(item.get("roleId").toString()));
            }

            params.clear();
            params.put("roleIds",roleIds);
            params.put("recordStatus", SysConst.RecordStatus.ABLE);
            List<Map<String,Object>> sysPermisCustoms = sysPermisMapper.selectListByRoleId(params);

            Map<String,Object> sysPermisItem;
            for (int i = 0, len = sysPermisCustoms.size(); i < len; i++){
                sysPermisItem = sysPermisCustoms.get(i);
                permissions.add(sysPermisItem.get("perFlag").toString());
            }
        }


//        permissions.add("admin:add");
//        permissions.add("admin:del");
        return ServerResponse.createBySuccess(permissions);
    }

    @Override
    public ServerResponse<SkPageVo<SysPermis>> queryObjsByPage(SysPermisQueryVo baseQueryVo) {
//        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
//        SysDictCustom condition = new SysDictCustom();
//
//        sysDictQueryVo.getIsNoLike().put("dictType",true);
//
//        condition.setDictType(SysConst.Dict.SysPermis.MENU_TYPE);
//        condition.setRecordStatus(SysConst.RecordStatus.ABLE);
//
//        sysDictQueryVo.setSysDictCustom(condition);

        SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
        SysDict condition = sysDictQueryVo.getCdtCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(SysConst.Dict.SysPermis.MENU_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        List<SysDict> sysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //类型
        Map<String,String> menuTypeMap = Maps.newHashMap();
        for(int i = 0,len = sysDicts.size(); i < len; i++) {
            SysDict sysDictCustom = sysDicts.get(i);
            menuTypeMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }
        //级别
        condition.setDictType(SysConst.Dict.SysPermis.MENU_LEVEL);
        sysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        Map<String,String> menuLevelMap = Maps.newHashMap();
        for(int i = 0,len = sysDicts.size(); i < len; i++) {
            SysDict sysDict = sysDicts.get(i);
            menuLevelMap.put(sysDict.getDictCode(),sysDict.getCodeName());
        }

        //数据封装
        ServerResponse<SkPageVo<SysPermis>> pageInfo = super.queryObjsByPage(baseQueryVo);
        List<SysPermis> data = pageInfo.getData().getList();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysPermis sysPermisCustom = data.get(i);
            sysPermisCustom.setPerType(menuTypeMap.get(sysPermisCustom.getPerType()));
            sysPermisCustom.setPerLevelStr(menuLevelMap.get(sysPermisCustom.getPerLevel().toString()));
        }
        return pageInfo;
    }

}
