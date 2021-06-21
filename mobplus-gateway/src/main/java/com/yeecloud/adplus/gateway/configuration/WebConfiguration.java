package com.yeecloud.adplus.gateway.configuration;

import com.yeecloud.meeto.configure.repository.ConfigureRepository;
import com.yeecloud.meeto.configure.service.ConfigureService;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * Title
 *
 * Date: 2019-11-06 17:36:32
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public ConfigureRepository buildConfigureRepository(@Autowired DataSource dataSource) {
        ConfigureRepository repository = new ConfigureRepository();
        repository.setDataSource(dataSource);
        return repository;
    }

    @Bean
    public ConfigureService createConfigureService(@Autowired ConfigureRepository configureRepository) {
        ConfigureService service = new ConfigureService();
        service.setConfigureRepository(configureRepository);
        service.refresh();
        return service;
    }

    @Bean
    public TomcatServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((Connector connector) -> {
            connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
            connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        });
        return factory;
    }
}
