package com.github.springtg.bot.platform.config.auto;

import com.github.springtg.bot.platform.client.command.Reply;
import com.github.springtg.bot.platform.handler.DefaultMessageHandler;
import com.github.springtg.bot.platform.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 23/07/2016
 */
@Configuration
@ConditionalOnMissingBean(DefaultMessageHandler.class)
@Slf4j
public class DefaultEventMessageHandlerResolverAutoConfiguration {

    @Autowired
    private MetricsService metricsService;

    @Bean
    public DefaultMessageHandler defaultMessageHandler() {
        return updateEvent -> {
            log.info("No suitable processor found for event {}", updateEvent);
            metricsService.onNoMessageProcessorFound();
            return Reply.EMPTY;
        };
    }
}
