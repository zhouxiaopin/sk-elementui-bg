package cn.sk.poi.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@Deseription excel字段注解
 *@Author zhoucp
 *@Date 2020/3/21 17:28
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAtrr {
    //位置 0开始
     int outSort() default -1;
    //位置 0开始
    int inSort() default -1;
}
