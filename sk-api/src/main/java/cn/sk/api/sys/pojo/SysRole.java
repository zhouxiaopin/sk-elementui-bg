package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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


    //权限ID
    @TableField(exist = false)
    private String permisIds;
    //旧权限ID
    @TableField(exist = false)
    private String oldPermisIds;
    //用户名
    @TableField(exist = false)
    private String userName;
    //资源ID
    @TableField(exist = false)
    private String resIds;
    //旧资源ID
    @TableField(exist = false)
    private String oldResIds;
    @TableField(exist = false)
    private String recordStatusStr;


    @Override
    public Serializable getPkVal() {
        return roleId;
    }
}