package cn.sk.api.sys.pojo;

import cn.sk.api.base.pojo.BaseQueryVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统数字字典实体类的包装对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysDictQueryVo extends BaseQueryVo {
    private SysDict cdtCustom = new SysDict();

    public static SysDictQueryVo newInstance() {
        return new SysDictQueryVo();
    }

    public static void getQueryKvInstance(String dictType) {

    }
}