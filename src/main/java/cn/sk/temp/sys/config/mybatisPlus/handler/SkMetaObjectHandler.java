package cn.sk.temp.sys.config.mybatisPlus.handler;

import cn.sk.temp.sys.common.SysConst;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *@Deseription 填充器
 *@Author zhoucp
 *@Date 2020/3/11 17:15
 **/
@Slf4j
@Component
public class SkMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("********insertFill开始填充**********");
        Object recordStatus = metaObject.getValue("recordStatus");
        if(null == recordStatus) {
            this.setFieldValByName("recordStatus", SysConst.RecordStatus.ABLE, metaObject);
        }
        Object createTime = metaObject.getValue("createTime");
        if(null == createTime) {
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("********updateFill开始填充**********");
        Object updateTime = metaObject.getValue("updateTime");
        if(null == updateTime) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }
}
