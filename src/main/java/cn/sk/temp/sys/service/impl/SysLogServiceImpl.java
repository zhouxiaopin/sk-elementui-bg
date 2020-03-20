package cn.sk.temp.sys.service.impl;

import cn.sk.temp.base.service.impl.BaseServiceImpl;
import cn.sk.temp.sys.mapper.SysLogMapper;
import cn.sk.temp.sys.pojo.SysLog;
import cn.sk.temp.sys.pojo.SysLogQueryVo;
import cn.sk.temp.sys.service.ISysLogService;
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
