package cn.sk.temp.sys.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 系统用户实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tb_sys_user")
public class SysUserCustom extends SysUser {
    //确认密码
    @TableField(exist = false)
    private String password2;
    //角色ID
    @TableField(exist = false)
    private String roleIds;
    //旧角色ID
    @TableField(exist = false)
    private String oldRoleIds;

    //角色名
    @TableField(exist = false)
    private String roleName;
    @TableField(exist = false)
    private String recordStatusStr;

//    public SysUserCustom(Integer uId, String userName, String password, String realName, String sex, String email, String mobilePhone, String salt, String descri, String recordStatus, Date updateTime, Date createTime) {
//        super(uId, userName, password, realName,sex, email, mobilePhone, salt, descri, recordStatus, updateTime, createTime);
//
//    }
}