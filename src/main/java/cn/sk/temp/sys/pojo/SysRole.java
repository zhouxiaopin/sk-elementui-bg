package cn.sk.temp.sys.pojo;

import cn.sk.temp.base.pojo.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_sys_role")
public class SysRole extends BaseModel {
    /**
     * 角色id
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色标识
     */
    private String roleFlag;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 描述
     */
    private String descri;

    /**
     * 操作者id
     */
    private Integer optId;


    @Override
    public Serializable getPkVal() {
        return roleId;
    }
}