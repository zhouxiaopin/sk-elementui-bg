package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户实体类的包装对象
 */
@Getter
@Setter
public class SysUserQueryVo extends BaseQueryVo {
    private SysUser cdtCustom = new SysUser();

    public static SysUserQueryVo newInstance() {
        return new SysUserQueryVo();
    }
}