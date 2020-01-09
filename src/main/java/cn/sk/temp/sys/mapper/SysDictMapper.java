package cn.sk.temp.sys.mapper;

import cn.sk.temp.base.mapper.IBaseMapper;
import cn.sk.temp.sys.pojo.SysDictCustom;
import cn.sk.temp.sys.pojo.SysDictQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictMapper extends IBaseMapper<SysDictCustom, SysDictQueryVo> {
}