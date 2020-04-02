package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sys_dict")
public class SysDict extends BaseModel {
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Integer dictId;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 编码名称
     */
    private String codeName;

    /**
     * 类型描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 预留字段1
     */
    private String field1;

    /**
     * 预留字段2
     */
    private String field2;

    /**
     * 预留字段3
     */
    private String field3;

    /**
     * 预留字段4
     */
    private String field4;

    /**
     * 预留字段5
     */
    private String field5;


    /**
     * 预留字段6
     */
    private String field6;

    @TableField(exist = false)
    private String recordStatusStr;

    @Override
    public Serializable getPkVal() {
        return dictId;
    }


}