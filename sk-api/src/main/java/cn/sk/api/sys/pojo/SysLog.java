package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_sys_log")
public class SysLog extends BaseModel {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作
     */
    private String operation;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * ip地址
     */
    private String ip;

    /**
     *  请求url
     */
    private String requestUrl;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 扩展字段
     */
    private String expan1;

    private String expan2;

    private String expan3;

    private String expan4;

    private String expan5;

    private String expan6;


    @Override
    public Serializable getPkVal() {
        return id;
    }
}