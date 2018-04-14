package com.github.telegram.bot.platform.config.auto;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.telegram.bot.platform.client.NextOffsetStrategy;
import com.github.telegram.bot.platform.client.impl.InMemoryNextOffsetStrategyImpl;

/**
 * @author Sergey Kuptsov
 * @since 25/08/2016
 */
@Configuration
@ConditionalOnMissingBean(NextOffsetStrategy.class)
public class DefaultNextOffsetStrategyImplAutoConfiguration {

    @Bean
    public NextOffsetStrategy defaultNextOffsetStrategy() {
        return new InMemoryNextOffsetStrategyImpl();
    }
}
