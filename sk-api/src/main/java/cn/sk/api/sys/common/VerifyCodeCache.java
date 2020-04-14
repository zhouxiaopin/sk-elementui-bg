package cn.sk.api.sys.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 *@Deseription 验证码缓存
 *@Author zhoucp
 *@Date 2020/1/6 17:16
 **/
@Slf4j
public class VerifyCodeCache {


    public static final String VC_PREFIX = "verifycode_";

    //LRU算法
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(500).expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    /**
     * 获取指定key的缓存对象
     * @param key
     * @return
     */
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            log.error("localCache get error",e);
        }
        return null;
    }

    /**
     * 显式地清除指定key的缓存对象
     * @param key
     * @return
     */
    public static boolean invalidate(String key){
        try {
            log.debug("localCache invalidate befor size:",localCache.size());
            localCache.invalidate(key);
            log.debug("localCache invalidate after size:",localCache.size());
            return true;
        }catch (Exception e){
            log.error("localCache invalidate error",e);
        }
        return false;
    }
    public static boolean refresh(String key){
        try {
            localCache.refresh(key);
            return true;
        }catch (Exception e){
            log.error("localCache refresh error",e);
        }
        return false;
    }
}
