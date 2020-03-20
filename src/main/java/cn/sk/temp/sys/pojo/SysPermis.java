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
@TableName("tb_sys_permis")
public class SysPermis extends BaseModel {
    /**
     * 权限id
     */
    @TableId(value = "per_id", type = IdType.AUTO)
    private Integer perId;

    /**
     * 权限标识
     */
    private String perFlag;

    /**
     * 菜单名
     */
    private String perName;

    /**
     * 菜单url
     */
    private String perUrl;

    /**
     * 前端路由路径
     */
    private String routePath;

    /**
     * 前端路由名
     */
    private String routeName;

    /**
     * 前端路由组件名
     */
    private String routeComponent;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 权限类型
     */
    private String perType;

    /**
     * 权限级别（菜单类型）
     */
    private Integer perLevel;

    /**
     * 排序
     */
    private Integer perSort;

    /**
     * 描述
     */
    private String descri;

    /**
     * 操作者id
     */
    private Integer optId;

    /**
     * 左边的icon
     */
    private String leftIcon;

    private String expand1;

    private String expand2;

    private String expand3;


    //用户名
    @TableField(exist = false)
    private String userName;
    //级别对应的值
    @TableField(exist = false)
    private String perLevelStr;
    //父权限名
    @TableField(exist = false)
    private String parentName;
    @TableField(exist = false)
    private String recordStatusStr;



    @Override
    public Serializable getPkVal() {
        return perId;
    }

}