package cn.sk.temp.sys.service;

import cn.sk.temp.base.service.IBaseService;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.pojo.SysDictCustom;
import cn.sk.temp.sys.pojo.SysDictQueryVo;

import java.util.Map;

/**
 * 系统数字字典业务逻辑接口
 */
public interface ISysDictService extends IBaseService<SysDictCustom, SysDictQueryVo>{
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
