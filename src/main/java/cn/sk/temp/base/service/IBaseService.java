package cn.sk.temp.base.service;

import cn.sk.temp.sys.common.ServerResponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 基本Service
 * @param <T>   实体扩展类
 * @param <V>   实体包装类
 */
public interface IBaseService<T,V> {
    //添加单个对象
    ServerResponse<T> insert(T entityCustom);

    //修改单个对象
    ServerResponse<T> update(T entityCustom);

    //软删除
    ServerResponse<T> deleteInIds(String[] ids);

    //硬删除
    ServerResponse<T> realDeleteInIds(String[] ids);

    //删除单个对象
    ServerResponse<T> delete(T entityCustom);

    //查询单个对象
    ServerResponse<T> queryObj(T entityCustom);

    //分页查询数据列表
//    ServerResponse<DataTableVo> queryObjsByPage(V entityQueryVo);
    ServerResponse<PageInfo<T>> queryObjsByPage(V entityQueryVo);

    //分页查询数据列表
//    ServerResponse<DataTableVo> queryObjsByPage(V entityQueryVo);
    ServerResponse<List<T>> queryObjs(V entityQueryVo);
}
