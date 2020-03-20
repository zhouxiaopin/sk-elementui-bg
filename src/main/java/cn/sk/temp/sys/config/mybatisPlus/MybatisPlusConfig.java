package cn.sk.temp.sys.config.mybatisPlus;

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

}
