package cn.sk.api.sys.service;

import cn.sk.api.base.service.IBaseService;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.pojo.SysDict;
import cn.sk.api.sys.pojo.SysDictQueryVo;

import java.util.Map;

/**
 * 系统数字字典业务逻辑接口
 */
public interface ISysDictService extends IBaseService<SysDict, SysDictQueryVo>{
    /**
     * 获取字典kv键值对
     * @param dictType
     * @return
     */
    ServerResponse<Map<String,String>> getDictKvData(String dictType);
    /**
     * 获取字典kv键值对(v和扩展字段用；连接)
     * @param dictType
     * @return
     */
    ServerResponse<Map<String,String>> getDictKvAndExpData(String dictType);
}
