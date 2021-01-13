package com.yeecloud.adplus.admin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Title
 *
 * Date: 2019-11-11 03:02:57
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Configuration
public class AsyncConfiguration {
    @Bean
    public AsyncTaskExecutor createAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Async-Executor-");
        executor.setMaxPoolSize(30);
        executor.setCorePoolSize(20);
        executor.setKeepAliveSeconds(500);
        executor.setQueueCapacity(10000000);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
