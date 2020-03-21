package cn.sk.api.sys.mapper;

import cn.sk.api.base.mapper.IBaseMapper;
import cn.sk.api.sys.pojo.SysUser;
import cn.sk.api.sys.pojo.SysUserQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends IBaseMapper<SysUser, SysUserQueryVo> {

}