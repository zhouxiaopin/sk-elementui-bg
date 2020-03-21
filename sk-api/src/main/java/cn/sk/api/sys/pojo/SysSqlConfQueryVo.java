package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统sql语句配置实体类的包装对象
 */
@Getter
@Setter
public class SysSqlConfQueryVo extends BaseQueryVo {
    private SysSqlConf cdtCustom = new SysSqlConf();

    public static SysSqlConfQueryVo newInstance() {
        return new SysSqlConfQueryVo();
    }
}