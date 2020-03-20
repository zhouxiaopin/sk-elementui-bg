package cn.sk.temp.sys.mapper;

import cn.sk.temp.base.mapper.IBaseMapper;
import cn.sk.temp.sys.pojo.SysResource;
import cn.sk.temp.sys.pojo.SysResourceQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysResourceMapper extends IBaseMapper<SysResource, SysResourceQueryVo> {
    List<Map<String,Object>> selectListByRoleId(@Param("params") Map<String,Object> params);
}