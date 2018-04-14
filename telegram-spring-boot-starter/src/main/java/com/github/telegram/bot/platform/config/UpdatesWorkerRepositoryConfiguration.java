package com.github.telegram.bot.platform.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
@Configuration
@Getter
public class UpdatesWorkerRepositoryConfiguration {

    @Value("${updates.worker.repository.queue.size:2048}")
    private Integer queueSize;
}
