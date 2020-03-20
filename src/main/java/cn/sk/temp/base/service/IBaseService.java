package cn.sk.temp.base.service;

import cn.sk.temp.base.pojo.BaseModel;
import cn.sk.temp.sys.common.ServerResponse;
import cn.sk.temp.sys.pojo.SkPageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 基本Service
 * @param <T>   实体扩展类
 */
public interface IBaseService<T,V> extends IService<T> {
    //添加单个对象
    ServerResponse<T> insert(T entity);

    //修改单个对象
    ServerResponse<T> update(T entity);

    //软删除
    ServerResponse<T> deleteInIds(String[] ids);

    //硬删除
    ServerResponse<T> realDeleteInIds(String[] ids);

    //删除单个对象
    ServerResponse delete(BaseModel model);

    //查询单个对象
    ServerResponse<T> queryObj(T entity);

    //分页查询数据列表
//    ServerResponse<DataTableVo> queryObjsByPage(V entityQueryVo);
    ServerResponse<SkPageVo<T>> queryObjsByPage(V entityQueryVo);

    //分页查询数据列表
//    ServerResponse<DataTableVo> queryObjsByPage(V entityQueryVo);
    ServerResponse<List<T>> queryObjs(V entityQueryVo);
}
