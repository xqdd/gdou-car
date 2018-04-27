package com.wteam.car.config;


import com.wteam.car.web.interceptor.AdminInterceptor;
import com.wteam.car.web.interceptor.StudentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
    private final com.wteam.car.web.interceptor.CrossDomainInterceptor crossDomainInterceptor;
    private final StudentInterceptor studentInterceptor;

    private final AdminInterceptor adminInterceptor;


    @Autowired
    public MyWebAppConfigurer(com.wteam.car.web.interceptor.CrossDomainInterceptor crossDomainInterceptor,
                              StudentInterceptor studentInterceptor,
                              AdminInterceptor adminInterceptor) {
        this.crossDomainInterceptor = crossDomainInterceptor;
        this.studentInterceptor = studentInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.crossDomainInterceptor).addPathPatterns("/**");
        registry.addInterceptor(this.studentInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns("/student/login");
        registry.addInterceptor(this.adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}
