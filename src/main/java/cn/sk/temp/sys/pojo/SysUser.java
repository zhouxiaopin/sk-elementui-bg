package cn.sk.temp.sys.pojo;

import cn.sk.temp.base.pojo.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_sys_user")
public class SysUser extends BaseModel{

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 盐值（用于md5加密）
     */
    private String salt;

    /**
     * 描述
     */
    private String descri;


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


    @Override
    public Serializable getPkVal() {
        return userId;
    }

}