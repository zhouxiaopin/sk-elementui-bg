package cn.sk.temp.sys.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 系统角色实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_sys_role")
public class SysRoleCustom extends SysRole {
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

//    public SysRoleCustom(Integer roleId, String roleFlag, String roleName, String descri, Integer optId, String recordStatus, Date updateTime, Date createTime) {
//        super(roleId, roleFlag, roleName, descri, optId, recordStatus, updateTime, createTime);
//    }
}