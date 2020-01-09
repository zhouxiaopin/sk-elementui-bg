package cn.sk.temp.sys.mapper;

import cn.sk.temp.base.mapper.IBaseMapper;
import cn.sk.temp.sys.pojo.SysRoleCustom;
import cn.sk.temp.sys.pojo.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysRoleMapper extends IBaseMapper<SysRoleCustom, SysRoleQueryVo> {

    List<Map<String,Object>> selectListByUserId(@Param("params") Map<String,Object> params);

}