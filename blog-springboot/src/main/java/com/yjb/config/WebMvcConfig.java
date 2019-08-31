package com.yjb.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //是否将swagger-ui.html 添加 到 resources目录下
        if (swaggerConfig.isEnableSwagger()) {
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");

            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");

            registry.addResourceHandler("/web_frontend/**")
                    .addResourceLocations("classpath:/web_frontend/");
        }
    }


}
