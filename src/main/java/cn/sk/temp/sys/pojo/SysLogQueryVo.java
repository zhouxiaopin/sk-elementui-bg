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
    private SysLogCustom cdtCustom = new SysLogCustom();

    public static SysLogQueryVo newInstance() {
        return new SysLogQueryVo();
    }
    private String startCreatTime;
    private String endCreatTime;

}