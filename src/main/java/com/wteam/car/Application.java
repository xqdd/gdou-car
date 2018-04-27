/**
 * 运行：
 * mvn spring-boot:run
 * <p>
 * 打包：
 * mvn clean package -Dmaven.test.skip=true
 */
package com.wteam.car;

import com.wteam.car.base.impl.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(com.wteam.car.Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(com.wteam.car.Application.class, args);
    }
}