/*
 * Copyright 2017 Focus Technology, Co., Ltd. All rights reserved.
 */
package cn.no7player.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * @author xuxiaowen
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Autowired
    public DataSource dataSource;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }





//        @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//     // 将所有/static/** 访问都映射到classpath:/static/ 目录下
//
//     registry.addResourceHandler("/webapp/**").addResourceLocations(
//     "classpath:/webapp/public/index.html");
//         registry.addResourceHandler("/storage/**").addResourceLocations(
//                 "/storage/");
//     }
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry){
//        registry.addViewController("/wb/**").setViewName("/webapp/index.html");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/webapp/**").addResourceLocations(
                "classpath:/static/webapp/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/webapp/css/**").addResourceLocations(
//                "classpath:/static/webapp/css/chunk-vendors.dfb29f82.js");
//        registry.addResourceHandler("/webapp/js/**").addResourceLocations(
//                "classpath:/static/webapp/js/chunk-vendors.dfb29f82.js");
    }


}
