package cn.sk.temp.sys.service;

import cn.sk.temp.base.service.IBaseService;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.pojo.SysSqlConf;
import cn.sk.temp.sys.pojo.SysSqlConfQueryVo;

/**
 * 系统sql语句配置业务逻辑接口
 */
public interface ISysSqlConfService extends IBaseService<SysSqlConf, SysSqlConfQueryVo>{
    /**
     * 根据sql配置编码查询真正的数据
     * @param scCode 配置编码
     * @return
     */
    ServerResponse queryRealByScCode(String scCode);
}
