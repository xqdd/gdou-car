package com.wteam.car.config;

import com.wteam.delay_queue.OrderJobTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ApplicationStartup implements
        ApplicationListener<ApplicationReadyEvent> {

    private final OrderJobTimer orderJobTimer;

    @Autowired
    public ApplicationStartup(OrderJobTimer orderJobTimer) {
        this.orderJobTimer = orderJobTimer;
    }

    //启动时运行
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //启动自动订单超时处理任务系统
        orderJobTimer.startThread();
    }


}
