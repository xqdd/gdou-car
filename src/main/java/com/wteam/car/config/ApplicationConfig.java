package com.wteam.car.config;

import com.wteam.delay_queue.OrderJobTimer;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class ApplicationConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final OrderJobTimer orderJobTimer;

    @Autowired
    public ApplicationConfig(OrderJobTimer orderJobTimer) {
        this.orderJobTimer = orderJobTimer;
    }

    //启动时运行
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //生成api文档
        Docs.DocsConfig docsConfig = new Docs.DocsConfig();
        docsConfig.setProjectPath("");
        docsConfig.setDocsPath("src/main/resources/static/apidocs/");
        Resources.setDebug();
        Docs.buildHtmlDocs(docsConfig);

        //启动自动订单超时处理任务系统
        orderJobTimer.startThread();
    }


}
