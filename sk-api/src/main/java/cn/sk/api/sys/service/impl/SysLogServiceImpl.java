package cn.sk.api.sys.service.impl;

import cn.sk.api.base.service.impl.BaseServiceImpl;
import cn.sk.api.sys.mapper.SysLogMapper;
import cn.sk.api.sys.pojo.SysLog;
import cn.sk.api.sys.pojo.SysLogQueryVo;
import cn.sk.api.sys.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统日志逻辑接口实现类
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, SysLogQueryVo,SysLogMapper> implements ISysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;



}
