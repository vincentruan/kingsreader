/*
 * Copyright [2018] [vincentruan]
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;
import org.springframework.util.StringUtils;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(SchedulerProperties.class)
@Slf4j
public class SchedulerConfig implements SchedulingConfigurer {

    private static final String THREAD_NAME_PREFIX = "$SCHEDULING-";

    private SchedulerProperties schedulerProperties;

    @Autowired
    private CustomRegisterBeanService customRegisterBeanService;

    public SchedulerConfig(SchedulerProperties schedulerProperties) {
        this.schedulerProperties = schedulerProperties;
    }

    /**
     * Callback allowing a {@link TaskScheduler
     * TaskScheduler} and specific {@link Task Task}
     * instances to be registered against the given the {@link ScheduledTaskRegistrar}
     *
     * @param taskRegistrar the registrar to be configured.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(schedulerProperties.getPoolSize());
        taskScheduler.setThreadNamePrefix(StringUtils.hasText(schedulerProperties.getThreadNamePrefix()) ? schedulerProperties.getThreadNamePrefix() : THREAD_NAME_PREFIX);

        // 设置拒绝策略
        RejectedExecutionHandler rejectedExecutionHandler = customRegisterBeanService.getRejectedExecutionHandlerBeanDefinition(schedulerProperties.getRejectionPolicy());
        if(null != rejectedExecutionHandler) {
            taskScheduler.setRejectedExecutionHandler(rejectedExecutionHandler);
        }

        return taskScheduler;
    }

}
