package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统日志实体类的包装对象
 */
@Getter
@Setter
public class SysLogQueryVo extends BaseQueryVo {
    private SysLog cdtCustom = new SysLog();

    public static SysLogQueryVo newInstance() {
        return new SysLogQueryVo();
    }
    private String startCreatTime;
    private String endCreatTime;

}