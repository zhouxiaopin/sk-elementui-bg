package cn.sk.temp.sys.mapper;

import cn.sk.temp.base.mapper.IBaseMapper;
import cn.sk.temp.sys.pojo.SysUser;
import cn.sk.temp.sys.pojo.SysUserQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends IBaseMapper<SysUser, SysUserQueryVo> {

}