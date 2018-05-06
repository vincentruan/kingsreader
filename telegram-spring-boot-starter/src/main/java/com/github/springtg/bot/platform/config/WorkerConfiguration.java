package com.github.springtg.bot.platform.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Configuration
@Getter
public class WorkerConfiguration {

    @Value("${updates.worker.task.thread.corePoolSize:4}")
    private int corePoolSize;

    @Value("${updates.worker.task.thread.maximumPoolSize:32}")
    private int maximumPoolSize;

    @Value("${updates.worker.task.thread.keepAliveSeconds:60}")
    private int keepAliveSeconds;

    @Value("${updates.worker.task.thread.queueCapacity:" + Integer.MAX_VALUE + "}")
    private int queueCapacity;

    @Value("${updates.worker.task.queue.size:2048}")
    private int queueSize;

    @Value("${updates.worker.task.thread.shutdown.timeout:5}")
    private long shutdownTimeout;

    public TimeUnit getShutdownTimeoutTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
