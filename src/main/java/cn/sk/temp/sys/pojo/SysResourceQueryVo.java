package cn.sk.temp.sys.pojo;

import cn.sk.temp.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源实体类的包装对象
 */
@Getter
@Setter
public class SysResourceQueryVo extends BaseQueryVo {
    private SysResource cdtCustom = new SysResource();

    public static SysResourceQueryVo newInstance() {
        return new SysResourceQueryVo();
    }

}