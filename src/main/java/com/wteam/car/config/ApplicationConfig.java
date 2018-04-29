package com.wteam.car.config;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.Resources;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class ApplicationConfig implements ApplicationListener<ContextRefreshedEvent> {


    //启动时运行
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //生成api文档
        Docs.DocsConfig docsConfig = new Docs.DocsConfig();
        docsConfig.setProjectPath("");
        docsConfig.setDocsPath("src/main/resources/static/apidocs/");
        Resources.setDebug();
        Docs.buildHtmlDocs(docsConfig);
    }


}
