package cn.sk.api.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@Deseription 基本实体类
 *@Author zhoucp
 *@Date 2020/3/11 14:25
 **/
@Data
public abstract class BaseModel implements Serializable{
    /**
     * (00=已删除，01=可用，02=禁用)
     */
    //标记为填充字段
    @TableField(fill = FieldFill.INSERT)
//    @TableLogic
    private String recordStatus;

    /**
     * 更新时间
     */
    //标记为填充字段
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    //标记为填充字段
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    //主键
//    @TableField(exist = false)
//    private Serializable pk;

    @JsonIgnore
    public abstract Serializable getPkVal();
}
