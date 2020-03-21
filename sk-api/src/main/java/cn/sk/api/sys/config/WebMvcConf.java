package cn.sk.api.sys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *@Deseription 
 *@Author zhoucp
 *@Date 2020/1/6 16:47
 **/
@Configuration
public class WebMvcConf implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")	// 允许跨域访问的路径
                .allowedOrigins("*")	// 允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")	// 允许请求方法
                .maxAge(168000)	// 预检间隔时间
                .allowedHeaders("*")  // 允许头部设置
                .allowCredentials(true);	// 是否发送cookie
    }

    /*@Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        *//* 是否允许请求带有验证信息 *//*
        corsConfiguration.setAllowCredentials(true);
        *//* 允许访问的客户端域名 *//*
        corsConfiguration.addAllowedOrigin("*");
        *//* 允许服务端访问的客户端请求头 *//*
        corsConfiguration.addAllowedHeader("*");
        *//* 允许访问的方法名,GET POST等 *//*
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }*/
}
