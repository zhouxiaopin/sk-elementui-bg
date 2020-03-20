package cn.sk.temp.sys.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统日志实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
@TableName("tb_sys_log")
public class SysLogCustom extends SysLog {


}