package com.wteam.car.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig implements ApplicationListener<ContextRefreshedEvent> {



    //启动时运行
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
