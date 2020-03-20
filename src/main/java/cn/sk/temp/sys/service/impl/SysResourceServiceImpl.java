package cn.sk.temp.sys.service.impl;

import cn.sk.temp.base.service.impl.BaseServiceImpl;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.mapper.SysResourceMapper;
import cn.sk.temp.sys.mapper.SysRoleMapper;
import cn.sk.temp.sys.pojo.*;
import cn.sk.temp.sys.service.ISysResourceService;
import cn.sk.temp.sys.utils.SysUtils;
import cn.sk.temp.sys.utils.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统资源业务逻辑接口实现类
 */
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResource, SysResourceQueryVo, SysResourceMapper> implements ISysResourceService {
    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public ServerResponse<List<Map<String, Object>>> querySysResourceTree(SysResourceQueryVo sysResourceQueryVo) {
        List<SysResource> sysResources = sysResourceMapper.selectListByQueryVo(sysResourceQueryVo);
        List<Map<String, Object>> data = Lists.newArrayList();
        for (int i = 0,len = sysResources.size(); i < len; i++){
            SysResource sysResourceCustom = sysResources.get(i);
//            {id:6, pId:0, name:"福建省", open:true, nocheck:true},
//            { id:1, pId:0, name:"一级分类", open:true},
            Map<String,Object> item = Maps.newHashMap();
            item.put("id",sysResourceCustom.getResId());
            item.put("pId",sysResourceCustom.getParentId());
            item.put("name",sysResourceCustom.getResName());
            item.put("level",sysResourceCustom.getResLevel());
            item.put("open",true);
            data.add(item);
//
        }
        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse querySysMenu() {

        SysUser sysUserInfo = SysUtils.getSysUser();
        Map<String,Object> params = Maps.newHashMap();
        params.put("userId",sysUserInfo.getUserId());
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        List<Map<String,Object>> sysRoleCustoms = sysRoleMapper.selectListByUserId(params);

        Set<Integer> roleIds = Sets.newHashSet();
        Map<String,Object> sysRoleItem;
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            for (int i = 0, len = sysRoleCustoms.size(); i < len; i++){
                sysRoleItem = sysRoleCustoms.get(i);
                roleIds.add(Integer.valueOf(sysRoleItem.get("roleId").toString()));
            }
        }

        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            params.clear();
            params.put("roleIds", roleIds);
            params.put("recordStatus", SysConst.RecordStatus.ABLE);
            params.put("resType", SysConst.Permis.MENU);
            params.put("orderBy", "res_sort");
//            List<Map<String,Object>> sysPermisCustoms = sysPermisMapper.selectListByRoleId(params);
            List<Map<String, Object>> sysResourceCustoms = sysResourceMapper.selectListByRoleId(params);
            List<TreeNode> treeNodes = Lists.newArrayList();
            Map<String, Object> sysResourceItem;
            TreeNode treeNode;

            //获取左边图片key-value对
//            SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
//            SysDictCustom condition = new SysDictCustom();
//
//            sysDictQueryVo.getIsNoLike().put("dictType", true);
//
//            condition.setDictType(SysConst.Dict.SysResource.LEFT_ICON);
//            condition.setRecordStatus(SysConst.RecordStatus.ABLE);
//
//            sysDictQueryVo.setSysDictCustom(condition);

            SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
            SysDict condition = sysDictQueryVo.getCdtCustom();

            sysDictQueryVo.getIsNoLike().put("dictType",true);

            condition.setDictType(SysConst.Dict.SysResource.LEFT_ICON);
            condition.setRecordStatus(SysConst.RecordStatus.ABLE);

            LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDict::getDictType,SysConst.Dict.SysResource.LEFT_ICON)
                    .eq(SysDict::getRecordStatus,SysConst.RecordStatus.ABLE);

//            List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
            List<SysDict> sysDicts = sysDictMapper.selectList(queryWrapper);

            //左边图标
            Map<String, String> leftIconMap = Maps.newHashMap();
            for (int i = 0, len = sysDicts.size(); i < len; i++) {
                SysDict sysDict = sysDicts.get(i);
                leftIconMap.put(sysDict.getDictCode(), sysDict.getField1());
            }

            for (int i = 0, len = sysResourceCustoms.size(); i < len; i++) {
                sysResourceItem = sysResourceCustoms.get(i);
                treeNode = new TreeNode();
                treeNode.setId(sysResourceItem.get("resId").toString());
                treeNode.setLevel(Integer.valueOf(sysResourceItem.get("resLevel").toString()));
                treeNode.setParentId(sysResourceItem.get("parentId").toString());
                treeNode.setName(sysResourceItem.get("resName").toString());
                Map<String, Object> attrs = Maps.newHashMap();
                attrs.put("leftIcon", leftIconMap.get(sysResourceItem.get("leftIcon").toString()));
                attrs.put("routePath", sysResourceItem.get("routePath"));
                attrs.put("routeName", sysResourceItem.get("routeName"));
                attrs.put("routeComponent", sysResourceItem.get("routeComponent"));
                treeNode.setAttrs(attrs);

                treeNodes.add(treeNode);
            }
            treeNodes = TreeUtil.bulid(treeNodes, SysConst.SysResource.DEFAULT_PARENTID.toString());
            Map<String,Object> dataMap = Maps.newHashMap();
            dataMap.put("sysMenu",sysResourceCustoms);
            dataMap.put("treeSysMenu",treeNodes);
            return ServerResponse.createBySuccess(dataMap);
        }
        return ServerResponse.createBySuccess(null);
    }

    @Override
    public ServerResponse<SkPageVo<SysResource>> queryObjsByPage(SysResourceQueryVo baseQueryVo) {
//        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
//        SysDictCustom condition = new SysDictCustom();
//
//        sysDictQueryVo.getIsNoLike().put("dictType",true);
//
//        condition.setDictType(SysConst.Dict.SysResource.RES_TYPE);
//        condition.setRecordStatus(SysConst.RecordStatus.ABLE);
//
//        sysDictQueryVo.setSysDictCustom(condition);
        SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
        SysDict condition = sysDictQueryVo.getCdtCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(SysConst.Dict.SysResource.RES_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        List<SysDict> sysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //类型
        Map<String,String> menuTypeMap = Maps.newHashMap();
        for(int i = 0,len = sysDicts.size(); i < len; i++) {
            SysDict sysDict= sysDicts.get(i);
            menuTypeMap.put(sysDict.getDictCode(),sysDict.getCodeName());
        }
        //级别
        condition.setDictType(SysConst.Dict.SysResource.RES_LEVEL);
        sysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        Map<String,String> menuLevelMap = Maps.newHashMap();
        for(int i = 0,len = sysDicts.size(); i < len; i++) {
            SysDict sysDict = sysDicts.get(i);
            menuLevelMap.put(sysDict.getDictCode(),sysDict.getCodeName());
        }
        //级别
        condition.setDictType(SysConst.Dict.SysResource.LEFT_ICON);
        sysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        Map<String,String> leftIconMap = Maps.newHashMap();
        for(int i = 0,len = sysDicts.size(); i < len; i++) {
            SysDict sysDictCustom = sysDicts.get(i);
            leftIconMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }

        //数据封装
        ServerResponse<SkPageVo<SysResource>> pageInfo = super.queryObjsByPage(baseQueryVo);
        List<SysResource> data = pageInfo.getData().getList();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysResource sysResourceCustom = data.get(i);
            sysResourceCustom.setResType(menuTypeMap.get(sysResourceCustom.getResType()));
            sysResourceCustom.setResLevelStr(menuLevelMap.get(sysResourceCustom.getResLevel().toString()));
            sysResourceCustom.setLeftIcon(leftIconMap.get(sysResourceCustom.getLeftIcon()));
        }
        return pageInfo;
    }

}
