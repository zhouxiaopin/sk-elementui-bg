package cn.sk.api.base.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

@Setter
@Getter
@ToString
public class BaseQueryVo{
    private Integer start = 0;//当前第几条
    private Integer length = 10;//页面显示数量

    //查询字典是否是用 like 还是 =
    private Map<String,Boolean> isNoLike = new HashedMap();
    //排序
//    private String orderBy = "update_time desc";
    private String orderBy = "create_time desc";

}
