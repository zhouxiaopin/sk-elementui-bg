package cn.sk.poi.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *@Deseription 批量导入返回实体
 *@Author zhoucp
 *@Date 2020/3/29 11:43
 **/
@Data
@Builder
public class BatchImportVo<T> {
    //总的条数
    private Integer totalNum;
    //成功条数
    private Integer sucNum;
    //失败条数
    private Integer failNum;
    //失败条数
    private List<Map<String,Object>> failList;

    public Integer getSucNum() {
        return totalNum-failList.size();
    }

    public Integer getFailNum() {
        return failList.size();
    }
}
