package com.github.springtg.bot.platform.repository;

import com.github.springtg.bot.platform.client.TelegramBotApi;
import com.github.springtg.bot.platform.config.UpdatesRepositoryConfiguration;
import com.github.springtg.bot.platform.model.UpdateEvent;
import com.github.springtg.bot.platform.model.UpdateEvents;
import com.github.springtg.bot.platform.service.MetricsService;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.api.objects.Update;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Repository
@Slf4j
public class UpdatesRepository extends AbstractExecutionThreadService {

    @Autowired
    private UpdatesRepositoryConfiguration updatesRepositoryConfiguration;

    @Autowired
    @Qualifier("updateRepositoryBotApi")
    private TelegramBotApi botApi;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private EventBus eventBus;

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            log.debug("Start pooling new updates");
            List<Update> updates = botApi.getNextUpdates(
                    updatesRepositoryConfiguration.getPoolingLimit(),
                    updatesRepositoryConfiguration.getPoolingTimeout());

            log.debug("Received following updates: {}", updates);

            eventBus.post(createUpdateEvents(updates));

            metricsService.onMessagesReceived(updates.size());
        }
    }

    private UpdateEvents createUpdateEvents(List<Update> updates) {
        return UpdateEvents.builder()
                .updateEventList(updates.stream().map(update -> UpdateEvent.builder()
                        .update(update)
                        .received(new Date())
                        .build())
                        .collect(toList()))
                .build();
    }

    @PostConstruct
    public void init() {
        startAsync()
                .awaitRunning();
    }

    @PreDestroy
    public void destroy() {
        stopAsync()
                .awaitTerminated();
    }
}
