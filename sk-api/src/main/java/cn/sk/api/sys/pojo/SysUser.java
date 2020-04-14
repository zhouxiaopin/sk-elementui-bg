package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseModel;
import cn.sk.poi.anno.ExcelAtrr;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.PictureData;

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
    @ExcelAtrr(outSort = 0,inSort = 0)
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    @ExcelAtrr(outSort = 1,inSort = 1)
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
    private String sexStr;
    @TableField(exist = false)
    private String recordStatusStr;

    /**
     * 头像文件
     */
    @TableField(exist = false)
    @JsonIgnore
    @ExcelAtrr(outSort = 11,inSort = 11)
    private PictureData headImg;

    //验证码key
    @TableField(exist = false)
    private String verifyCodeKey;
    //验证码
    @TableField(exist = false)
    private String verifyCode;


    @Override
    public Serializable getPkVal() {
        return userId;
    }

}