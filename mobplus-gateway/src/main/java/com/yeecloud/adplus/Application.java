package com.yeecloud.adplus;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.persistence.EntityManager;

/**
 * Title
 *
 * Date: 2020-03-19 02:33:31
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@EnableOpenApi
@EnableJpaAuditing
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
