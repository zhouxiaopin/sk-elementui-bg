package cn.sk.temp.sys.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统权限实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class SysPermisCustom extends SysPermis {

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

//    public SysPermisCustom(Integer pId, String pFlag, String pName, String pUrl, Integer parentId, String pType, Integer pLevel, Integer pSort, String descri, Integer optId, String leftIcon, String expand1, String expand2, String expand3, String recordStatus, Date updateTime, Date createTime) {
//        super(pId, pFlag, pName, pUrl, parentId, pType, pLevel, pSort, descri, optId, leftIcon, expand1, expand2, expand3, recordStatus, updateTime, createTime);
//    }

}