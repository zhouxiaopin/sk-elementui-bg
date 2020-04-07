package cn.sk.api.base.service.impl;

import cn.sk.api.base.mapper.IBaseMapper;
import cn.sk.api.base.pojo.BaseModel;
import cn.sk.api.base.pojo.BaseQueryVo;
import cn.sk.api.base.service.IBaseService;
import cn.sk.api.business.common.Const;
import cn.sk.common.common.CustomException;
import cn.sk.common.common.ResponseCode;
import cn.sk.common.common.ServerResponse;
import cn.sk.api.sys.common.SysConst;
import cn.sk.api.sys.mapper.SysDictMapper;
import cn.sk.api.sys.pojo.SkPageVo;
import cn.sk.api.sys.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseServiceImpl<T extends BaseModel,V,M extends IBaseMapper<T,V>> extends ServiceImpl<M,T>  implements IBaseService<T,V> {
    @Autowired
    protected IBaseMapper<T,V> skBaseMapper;
    @Autowired
    protected SysDictMapper sysDictMapper;
    @Autowired
    protected ISysDictService sysDictService;


    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> insert(T entityCustom) {
        ServerResponse serverResponse = insertBefore(entityCustom);
        if(!serverResponse.isSuccess()) {
            return serverResponse;
        }
//        int num = skBaseMapper.insert(entityCustom);
        boolean flag = this.save(entityCustom);
//        if(num > 0) {
        if(flag) {
            serverResponse = insertAfter(entityCustom);
            if(!serverResponse.isSuccess()) {
                throw new CustomException(ResponseCode.ADD_FAIL);
            }
            return ServerResponse.createBySuccess(SysConst.ResponseMsg.ADD_SUCCE,entityCustom);
        }else {
//            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.ADD_FAIL);
            throw new CustomException(ResponseCode.ADD_FAIL);
        }
    }

    //添加之前的操作
    protected ServerResponse<T> insertBefore(T t){
        return ServerResponse.createBySuccess(t);
    }
    //添加之后的操作
    protected ServerResponse<T> insertAfter(T t){
        return ServerResponse.createBySuccess(t);
    }

    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> update(T entityCustom) {
        ServerResponse serverResponse = updateBefore(entityCustom);
        if(!serverResponse.isSuccess()) {
            return serverResponse;
        }

//        int num = skBaseMapper.updateById(entityCustom);
        boolean flag = this.updateById(entityCustom);
        if(!flag) {
            throw new CustomException(SysConst.ResponseMsg.UPDATE_FAIL);

        }
        return ServerResponse.createBySuccess(SysConst.ResponseMsg.UPDATE_SUCCE,entityCustom);
    }

    //删除之后的操作
    protected ServerResponse<T> deleteInIdsAfter(String[] ids){
        return ServerResponse.createBySuccess();
    }
    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> deleteInIds(String[] ids) {
        int num = skBaseMapper.deleteInIds(ids, SysConst.RecordStatus.DELETE);
        if(num > 0) {
            ServerResponse serverResponse = deleteInIdsAfter(ids);
            if(!serverResponse.isSuccess()) {
                throw new CustomException(ResponseCode.DEL_FAIL);
            }
            return ServerResponse.createBySuccessMessage(SysConst.ResponseMsg.DELET_SUCCE);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.DELET_FAIL);
        }
    }

    //硬删除之后的操作
    protected ServerResponse<T> realDeleteInIdsAfter(String[] ids){
        return ServerResponse.createBySuccess();
    }
    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> realDeleteInIds(String[] ids) {
        int num = skBaseMapper.realDeleteInIds(ids);

        if(num > 0) {
            ServerResponse serverResponse = realDeleteInIdsAfter(ids);
            if(!serverResponse.isSuccess()) {
                throw new CustomException(ResponseCode.DEL_FAIL);
            }
            return ServerResponse.createBySuccessMessage(SysConst.ResponseMsg.DELET_SUCCE);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.DELET_FAIL);
        }
    }

    //修改之前的操作
    protected ServerResponse<T> updateBefore(T t){
        return ServerResponse.createBySuccess();
    }

    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse delete(BaseModel model) {
//        int num = skBaseMapper.deleteByPrimaryKey(entityCustom);
        boolean flag = this.removeById(model.getPkVal());
//        if(num > 0) {
        if(flag) {
            return ServerResponse.createBySuccess(SysConst.ResponseMsg.DELET_SUCCE);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.DELET_FAIL);
        }
    }

    @Override
    public ServerResponse<T> queryObj(BaseModel model) {
//        T t = skBaseMapper.selectByPrimaryKey(entityCustom);
        T t = this.getById(model.getPkVal());

        if(null == t) {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.QUERY_FAIL);
        }
        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,t);
    }

//    @Override
//    public ServerResponse<DataTableVo> queryObjsByPage(V entityQueryVo) {
//        //startPage--start
//        //填充自己的sql查询逻辑
//        //pageHelper-收尾
//        BaseQueryVo baseQueryVo = (BaseQueryVo)entityQueryVo;
//        PageHelper.startPage(baseQueryVo.getStart(),baseQueryVo.getLength());
//        List<T> list = skBaseMapper.selectListByQueryVo(entityQueryVo);
//
//        PageInfo pageResult = new PageInfo(list);
//
//        DataTableVo<T> dataTableVo = new DataTableVo();
//
//        dataTableVo.setDraw(baseQueryVo.getDraw());
//        dataTableVo.setRecordsTotal(pageResult.getTotal());
//        dataTableVo.setData(list);
//        return ServerResponse.createBySuccess(dataTableVo);
//    }
    @Override
    public ServerResponse<SkPageVo<T>> queryObjsByPage(V entityQueryVo) {

        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        BaseQueryVo baseQueryVo = (BaseQueryVo)entityQueryVo;
//        PageHelper.startPage(baseQueryVo.getStart(),baseQueryVo.getLength());
//        PageHelper.offsetPage(baseQueryVo.getStart(),baseQueryVo.getLength());
        long total = skBaseMapper.selectCountByQueryVo(entityQueryVo);
        List<T> list = skBaseMapper.selectListByQueryVo(entityQueryVo);
        //记录状态
        Map<String,String> recordStatusMap = sysDictService.getDictKvAndExpData(Const.Dict.RECORD_STATUS).getData();

        //数据封装
        if(MapUtils.isNotEmpty(recordStatusMap)) {
            for(int i = 0,len = list.size(); i < len; i++) {
                T item = list.get(i);
                try{
                    Class<? extends Object> tClass = item.getClass();
                    Class<? extends Object> tsuperClass = tClass.getSuperclass();
                    Class<? extends Object> tssClass = tsuperClass.getSuperclass();
                    // 获取属性值
                    Field recordStatusField = null;
                    try {
                        recordStatusField = tClass.getDeclaredField("recordStatus");
                    }catch (Exception e){

                        try {
                            recordStatusField = tsuperClass.getDeclaredField("recordStatus");
                        }catch (Exception ex){
                            recordStatusField = tssClass.getDeclaredField("recordStatus");
                        }
                    }
                    recordStatusField.setAccessible(true);
                    String recordStatus = (String) recordStatusField.get(item);
                    Method m = tClass.getDeclaredMethod("setRecordStatusStr", String.class);

                    m.invoke(item,recordStatusMap.get(recordStatus));
                }catch(Exception e){
                    log.info("反射没有这个属性");
                }
            }

        }

//        PageInfo pageResult = new PageInfo(list);
        SkPageVo skPageVo = new SkPageVo(baseQueryVo.getStart(),baseQueryVo.getLength(),total);
        skPageVo.setList(list);
//        Page pageResult = new Page();
//        pageResult.setRecords(list);

        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,skPageVo);
    }

    @Override
    public ServerResponse<List<T>> queryObjs(V entityQueryVo) {
        BaseQueryVo baseQueryVo = (BaseQueryVo)entityQueryVo;
        baseQueryVo.setStart(null);
        List<T> list = skBaseMapper.selectListByQueryVo(entityQueryVo);
        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,list);
    }

}
