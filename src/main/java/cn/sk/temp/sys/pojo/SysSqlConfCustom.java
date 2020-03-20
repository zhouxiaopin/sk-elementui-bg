package cn.sk.temp.sys.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * sql语句配置实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
@TableName("tb_sys_sql_conf")
public class SysSqlConfCustom extends SysSqlConf{
    @TableField(exist = false)
    private String recordStatusStr;
//    public SysSqlConfCustom(Integer scId, String scCode, String scName, String scStatement, String scType, String descri, Integer optId, String field1, String field2, String field3, String field4, String recordStatus, Date updateTime, Date createTime) {
//        super(scId, scCode, scName, scStatement, scType, descri, optId, field1, field2, field3, field4, recordStatus, updateTime, createTime);
//    }

}