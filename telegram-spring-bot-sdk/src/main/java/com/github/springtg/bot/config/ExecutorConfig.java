/*
 * Copyright [2016] [vincentruan]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.springtg.bot.config;

import com.github.springtg.bot.service.CustomRegisterBeanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Configuration
@EnableAsync
@EnableRetry
@EnableConfigurationProperties(ExecutorProperties.class)
@Slf4j
public class ExecutorConfig implements AsyncConfigurer {

    private static final String THREAD_NAME_PREFIX = "$EXECUTOR-";

    private ExecutorProperties executorProperties;

    @Autowired
    private CustomRegisterBeanService customRegisterBeanService;

    @Autowired
    private ApplicationContext applicationContext;

    public ExecutorConfig(ExecutorProperties executorProperties) {
        this.executorProperties = executorProperties;
    }

    /**
     * The {@link Executor} instance to be used when processing async
     * method invocations.
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        if(executorProperties.getCorePoolSize() <= 0) {
            executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2 - 1);
        } else {
            executor.setCorePoolSize(executorProperties.getCorePoolSize());
        }
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        executor.setThreadNamePrefix(StringUtils.hasText(executorProperties.getThreadNamePrefix()) ? executorProperties.getThreadNamePrefix() : THREAD_NAME_PREFIX);

        // 设置拒绝策略
        RejectedExecutionHandler rejectedExecutionHandler = customRegisterBeanService.getRejectedExecutionHandlerBeanDefinition(executorProperties.getRejectionPolicy());
        if(null != rejectedExecutionHandler) {
            executor.setRejectedExecutionHandler(rejectedExecutionHandler);
        }
        
        // Initialize the executor
        executor.afterPropertiesSet();
        return executor;
    }

    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }


}
