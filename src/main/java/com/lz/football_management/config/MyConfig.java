package com.lz.football_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {
    //由于springboot发布为jar导致，上传文件不能存储到项目中，解决办法是在项目外创建一个文件夹用户接收存储文件
    //但是，要将此文件夹添加到资源文件中，方便引用
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathImg = "/Users/kevin/Downloads/upload/img/";
        String pathLogo = "/Users/kevin/Downloads/upload/logo/";
        registry.addResourceHandler("/upload/img/**").addResourceLocations("file:"+pathImg);
        registry.addResourceHandler("/upload/logo/**").addResourceLocations("file:"+pathLogo);
    }

}
