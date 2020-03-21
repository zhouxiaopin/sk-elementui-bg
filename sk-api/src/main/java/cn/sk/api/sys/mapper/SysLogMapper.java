package cn.sk.api.sys.mapper;

import cn.sk.api.base.mapper.IBaseMapper;
import cn.sk.api.sys.pojo.SysLog;
import cn.sk.api.sys.pojo.SysLogQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogMapper extends IBaseMapper<SysLog, SysLogQueryVo> {

}