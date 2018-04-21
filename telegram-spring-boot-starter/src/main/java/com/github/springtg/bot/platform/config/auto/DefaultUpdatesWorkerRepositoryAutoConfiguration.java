package com.github.springtg.bot.platform.config.auto;

import com.github.springtg.bot.platform.worker.saver.UpdatesWorkerRepository;
import com.github.springtg.bot.platform.worker.saver.impl.BlockingQueueUpdatesWorkerRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 29/08/2016
 */
@Configuration
@ConditionalOnMissingBean(UpdatesWorkerRepository.class)
public class DefaultUpdatesWorkerRepositoryAutoConfiguration {

    @Bean
    public UpdatesWorkerRepository updatesWorkerRepository() {
        return new BlockingQueueUpdatesWorkerRepository();
    }
}
