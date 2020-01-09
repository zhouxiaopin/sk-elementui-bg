package cn.sk.temp.sys.mapper;

import cn.sk.temp.base.mapper.IBaseMapper;
import cn.sk.temp.sys.pojo.SysLogCustom;
import cn.sk.temp.sys.pojo.SysLogQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogMapper extends IBaseMapper<SysLogCustom, SysLogQueryVo> {

}