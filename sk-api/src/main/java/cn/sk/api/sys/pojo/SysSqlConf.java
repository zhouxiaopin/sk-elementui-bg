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
@TableName("tb_sys_sql_conf")
public class SysSqlConf extends BaseModel {
    /**
     * 主键
     */
    @TableId(value = "sc_id", type = IdType.AUTO)
    private Integer scId;

    /**
     * 语句编码
     */
    private String scCode;

    /**
     * 语句名
     */
    private String scName;

    /**
     * 语句
     */
    private String scStatement;

    /**
     * 类型（01=sql语句，02=存储过程）
     */
    private String scType;

    /**
     * 描述
     */
    private String descri;

    private Integer optId;

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

    @TableField(exist = false)
    private String recordStatusStr;


    @Override
    public Serializable getPkVal() {
        return scId;
    }
}