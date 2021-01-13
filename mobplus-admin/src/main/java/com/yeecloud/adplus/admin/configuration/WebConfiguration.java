package com.yeecloud.adplus.admin.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.security.CurrentUserMethodArgumentResolver;
import com.yeecloud.meeto.common.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

    @Value("${file.path.game.thumb}")
    private String gameThumbPath;

    @Value("${file.path.app.icon}")
    private String appIconPath;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(createUserResolver());
    }

    private CurrentUserMethodArgumentResolver createUserResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    @Bean
    public HttpMessageConverter buildHttpMessageConverter() {
        FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setDateFormat(Constants.DATE_FORMAT_YYYYMMDD);

        List<MediaType> fastMediaTypes = Lists.newArrayList();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        messageConverter.setSupportedMediaTypes(fastMediaTypes);
        messageConverter.setFastJsonConfig(fastJsonConfig);
        return messageConverter;
    }

    @Bean
    @Profile("dev")
    public FilterRegistrationBean corsFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public GeneralConvert buildGeneralConvert() {
        return new GeneralConvert();
    }

    // 本地资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/game/thumb/**").addResourceLocations("file:"+gameThumbPath);
        registry.addResourceHandler("/app/icon/**").addResourceLocations("file:"+appIconPath);
    }
}

