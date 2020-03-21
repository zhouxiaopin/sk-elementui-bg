package cn.sk.api.sys.pojo;

import lombok.Data;

import java.util.List;

/**
 *@Deseription 分页实体类
 *@Author zhoucp
 *@Date 2020/3/13 8:57
 **/
@Data
public class SkPageVo<T> {
    //数据
    private List<T> list;
    //总的
    private long total;
    //总页数
    private long pageSize;
    //当前页
    private long curPage;

    /**
     *
     * @param start 开始位置
     * @param length 一页多长
     */
    public SkPageVo(long start,long length){
        long curPage = start%length == 0?start/length:(start/length)+1;
        this.curPage = curPage;
        this.pageSize = length;
    }
    /**
     *
     * @param start 开始位置
     * @param length 一页多长
     */
    public SkPageVo(long start,long length,long total){
        long curPage = start%length == 0?(start/length+1):(start/length)+2;
//        long curPage = start%length == 0?start/length:(start/length);
        this.curPage = curPage;
        this.pageSize = length;
        this.total = total;
    }
}
