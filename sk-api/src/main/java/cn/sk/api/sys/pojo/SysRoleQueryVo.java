package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色实体类的包装对象
 */
@Getter
@Setter
public class SysRoleQueryVo extends BaseQueryVo {
    private SysRole cdtCustom = new SysRole();

    public static SysRoleQueryVo newInstance() {
        return new SysRoleQueryVo();
    }
}