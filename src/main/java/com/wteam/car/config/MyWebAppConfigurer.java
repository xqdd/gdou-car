package com.wteam.car.config;


import com.wteam.car.web.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    private final com.wteam.car.web.interceptor.CrossDomainInterceptor crossDomainInterceptor;
    private final UserInterceptor userInterceptor;


    @Autowired
    public MyWebAppConfigurer(com.wteam.car.web.interceptor.CrossDomainInterceptor crossDomainInterceptor,
                              UserInterceptor userInterceptor) {
        this.crossDomainInterceptor = crossDomainInterceptor;
        this.userInterceptor = userInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.crossDomainInterceptor).addPathPatterns("/**");
        registry.addInterceptor(this.userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/wxWeb/**")
                .excludePathPatterns("/wxMiniApp/**")
                .excludePathPatterns("/apidoc/**")
                .excludePathPatterns("/**/*.txt")
                .excludePathPatterns("/**/*.html")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/**/*.gif");

    }
}
