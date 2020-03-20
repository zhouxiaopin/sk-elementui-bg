package cn.sk.temp.sys.pojo;

import cn.sk.temp.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统权限实体类的包装对象
 */
@Getter
@Setter
public class SysPermisQueryVo extends BaseQueryVo {
    private SysPermis cdtCustom = new SysPermis();

    public static SysPermisQueryVo newInstance() {
        return new SysPermisQueryVo();
    }
    //权限级别
//    private Integer greaterThanPLevel;

}