package cn.sk.api.sys.mapper;

import cn.sk.api.base.mapper.IBaseMapper;
import cn.sk.api.sys.pojo.SysPermis;
import cn.sk.api.sys.pojo.SysPermisQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysPermisMapper extends IBaseMapper<SysPermis, SysPermisQueryVo> {
    List<Map<String,Object>> selectListByRoleId(@Param("params") Map<String, Object> params);
}