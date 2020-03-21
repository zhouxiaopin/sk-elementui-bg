package cn.sk.api.sys.config.mybatisPlus;

import cn.sk.api.sys.config.mybatisPlus.injector.SkSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//扫描 mybatis dao 包路径
//@MapperScan(basePackages = {"cn.sk.temp.sys.mapper","cn.sk.temp.business.mapper"})
//@MapperScan(basePackages = {"cn.sk.temp.*.mapper"})
@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public SkSqlInjector myLogicSqlInjector() {
        return new SkSqlInjector();
    }

}
