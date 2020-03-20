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
@TableName("tb_sys_sesource")
public class SysResource extends BaseModel {
    /**
     * 资源id
     */
    @TableId(value = "res_id", type = IdType.AUTO)
    private Integer resId;

    /**
     * 资源名
     */
    private String resName;

    /**
     * 资源url
     */
    private String resUrl;

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
     * 资源类型
     */
    private String resType;

    /**
     * 资源级别（菜单类型）
     */
    private Integer resLevel;

    /**
     * 排序
     */
    private Integer resSort;

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


    @Override
    public Serializable getPkVal() {
        return resId;
    }

}