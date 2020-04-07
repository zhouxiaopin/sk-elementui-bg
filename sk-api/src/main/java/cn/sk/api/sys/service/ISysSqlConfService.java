package cn.sk.api.sys.service;

import cn.sk.api.base.service.IBaseService;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.pojo.SysSqlConf;
import cn.sk.api.sys.pojo.SysSqlConfQueryVo;

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
