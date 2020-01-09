package cn.sk.temp.business.mapper;

import cn.sk.temp.base.mapper.IBaseMapper;
import cn.sk.temp.business.pojo.RepairCustom;
import cn.sk.temp.business.pojo.RepairQueryVo;
import org.springframework.stereotype.Repository;

/**
 *@Deseription 报修信息Mapper
 *@Author zhoucp
 *@Date 2019/7/3 15:36
 **/
@Repository
public interface RepairMapper extends IBaseMapper<RepairCustom, RepairQueryVo> {

//    int updateByPrimaryKeyWithBLOBs(Repair record);

}