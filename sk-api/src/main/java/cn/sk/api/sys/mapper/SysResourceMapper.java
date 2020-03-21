package cn.sk.api.sys.mapper;

import cn.sk.api.base.mapper.IBaseMapper;
import cn.sk.api.sys.pojo.SysResource;
import cn.sk.api.sys.pojo.SysResourceQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysResourceMapper extends IBaseMapper<SysResource, SysResourceQueryVo> {
    List<Map<String,Object>> selectListByRoleId(@Param("params") Map<String, Object> params);
}