package cn.sk.api.sys.service.impl;

import cn.sk.api.base.service.impl.BaseServiceImpl;
import cn.sk.api.business.common.Const;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.mapper.SysDictMapper;
import cn.sk.api.sys.pojo.SysDict;
import cn.sk.api.sys.pojo.SysDictQueryVo;
import cn.sk.api.sys.service.ISysDictService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统数字字典业务逻辑接口实现类
 */
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict, SysDictQueryVo,SysDictMapper> implements ISysDictService {
    @Autowired
    private SysDictMapper sysDictMapper;


    @Override
    public ServerResponse<Map<String, String>> getDictKvData(String dictType) {
        SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
        SysDict condition = sysDictQueryVo.getCdtCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(dictType)
                .setRecordStatus(SysConst.RecordStatus.ABLE);

        List<SysDict> SysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //封装数据
        Map<String,String> kvMap = Maps.newHashMap();
        for(int i = 0,len = SysDicts.size(); i < len; i++) {
            SysDict SysDict = SysDicts.get(i);
            kvMap.put(SysDict.getDictCode(),SysDict.getCodeName());
        }
        return ServerResponse.createBySuccess(kvMap);
    }

    @Override
    public ServerResponse<Map<String, String>> getDictKvAndExpData(String dictType) {
        SysDictQueryVo sysDictQueryVo = SysDictQueryVo.newInstance();
        SysDict condition = sysDictQueryVo.getCdtCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(dictType)
                .setRecordStatus(SysConst.RecordStatus.ABLE);
        List<SysDict> SysDicts = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //封装数据
        Map<String,String> kvMap = Maps.newHashMap();
        for(int i = 0,len = SysDicts.size(); i < len; i++) {
            SysDict SysDict = SysDicts.get(i);
            StringBuilder codeName = new StringBuilder();
            codeName.append(SysDict.getCodeName());
            String filed1 = SysDict.getField1();
            String filed2 = SysDict.getField2();
            String filed3 = SysDict.getField3();
            String filed4 = SysDict.getField4();
            String filed5 = SysDict.getField5();
            String filed6 = SysDict.getField6();
            if(StringUtils.isNotBlank(filed1)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed1);
            }
            if(StringUtils.isNotBlank(filed2)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed2);
            }
            if(StringUtils.isNotBlank(filed3)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed3);
            }
            if(StringUtils.isNotBlank(filed4)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed4);
            }
            if(StringUtils.isNotBlank(filed5)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed5);
            }
            if(StringUtils.isNotBlank(filed6)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed6);
            }
            kvMap.put(SysDict.getDictCode(),codeName.toString());
        }
        return ServerResponse.createBySuccess(kvMap);
    }

//    @Override
//    public ServerResponse<PageInfo<SysDict>> queryObjsByPage(SysDictQueryVo entityQueryVo) {
//        //记录状态
//        Map<String,String> recordStatusMap = sysDictService.getDictKvAndExpData(Const.Dict.RECORD_STATUS).getData();
//
//        //数据封装
//        ServerResponse<PageInfo<SysDict>> serverResponse = super.queryObjsByPage(entityQueryVo);
//        List<SysDict> data = serverResponse.getData().getList();
//        for(int i = 0,len = data.size(); i < len; i++) {
//            SysDict item = data.get(i);
//            if(ObjectUtils.allNotNull(recordStatusMap)) {
//                item.setRecordStatusStr(recordStatusMap.get(item.getRecordStatus()));
//            }
//        }
//        return serverResponse;
//    }
}
