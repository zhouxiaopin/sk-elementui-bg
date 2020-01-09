package cn.sk.temp.sys.pojo;

import cn.sk.temp.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统日志实体类的包装对象
 */
@Getter
@Setter
public class SysLogQueryVo extends BaseQueryVo {
    private SysLogCustom sysLogCustom;
    private String startCreatTime;
    private String endCreatTime;

}