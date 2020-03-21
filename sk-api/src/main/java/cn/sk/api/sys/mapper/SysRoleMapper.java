package cn.sk.api.sys.mapper;

import cn.sk.api.base.mapper.IBaseMapper;
import cn.sk.api.sys.pojo.SysRole;
import cn.sk.api.sys.pojo.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysRoleMapper extends IBaseMapper<SysRole, SysRoleQueryVo> {

    List<Map<String,Object>> selectListByUserId(@Param("params") Map<String, Object> params);

}