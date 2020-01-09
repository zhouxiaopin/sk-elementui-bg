package cn.sk.temp.sys.service.impl;

import cn.sk.temp.base.service.impl.BaseServiceImpl;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.common.SysConst;
import cn.sk.temp.sys.mapper.SysPermisMapper;
import cn.sk.temp.sys.pojo.SysDictCustom;
import cn.sk.temp.sys.pojo.SysDictQueryVo;
import cn.sk.temp.sys.pojo.SysPermisCustom;
import cn.sk.temp.sys.pojo.SysPermisQueryVo;
import cn.sk.temp.sys.service.ISysPermisService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统权限业务逻辑接口实现类
 */
@Service
public class SysPermisServiceImpl extends BaseServiceImpl<SysPermisCustom,SysPermisQueryVo> implements ISysPermisService {
    @Autowired
    private SysPermisMapper sysPermisMapper;


    @Override
    public ServerResponse<List<Map<String, Object>>> querySysPermisTree(SysPermisQueryVo sysPermisQueryVo) {
        List<SysPermisCustom> sysPermisCustoms = sysPermisMapper.selectListByQueryVo(sysPermisQueryVo);
        List<Map<String, Object>> data = Lists.newArrayList();
        for (int i = 0,len = sysPermisCustoms.size(); i < len; i++){
            SysPermisCustom sysPermisCustom = sysPermisCustoms.get(i);
//            {id:6, pId:0, name:"福建省", open:true, nocheck:true},
//            { id:1, pId:0, name:"一级分类", open:true},
            Map<String,Object> item = Maps.newHashMap();
            item.put("id",sysPermisCustom.getpId());
            item.put("pId",sysPermisCustom.getParentId());
            item.put("name",sysPermisCustom.getpName());
            item.put("level",sysPermisCustom.getpLevel());
            item.put("open",true);
            data.add(item);
//
        }
        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse<PageInfo<SysPermisCustom>> queryObjsByPage(SysPermisQueryVo entityQueryVo) {
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
        SysDictCustom condition = new SysDictCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(SysConst.Dict.SysPermis.MENU_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.setSysDictCustom(condition);
        List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //类型
        Map<String,String> menuTypeMap = Maps.newHashMap();
        for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
            SysDictCustom sysDictCustom = sysDictCustoms.get(i);
            menuTypeMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }
        //级别
        condition.setDictType(SysConst.Dict.SysPermis.MENU_LEVEL);
        sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        Map<String,String> menuLevelMap = Maps.newHashMap();
        for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
            SysDictCustom sysDictCustom = sysDictCustoms.get(i);
            menuLevelMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }

        //数据封装
        ServerResponse<PageInfo<SysPermisCustom>> pageInfo = super.queryObjsByPage(entityQueryVo);
        List<SysPermisCustom> data = pageInfo.getData().getList();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysPermisCustom sysPermisCustom = data.get(i);
            sysPermisCustom.setpType(menuTypeMap.get(sysPermisCustom.getpType()));
            sysPermisCustom.setPLevelStr(menuLevelMap.get(sysPermisCustom.getpLevel().toString()));
        }
        return pageInfo;
    }

}
