package com.github.telegram.bot.platform.worker.saver.impl;

import com.codahale.metrics.annotation.Timed;
import com.github.telegram.bot.platform.config.UpdatesWorkerRepositoryConfiguration;
import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.model.UpdateEvents;
import com.github.telegram.bot.platform.worker.saver.UpdatesSaver;
import com.github.telegram.bot.platform.worker.saver.UpdatesWorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.github.telegram.bot.platform.model.UpdateEvent.EMPTY;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
@Slf4j
public class BlockingQueueUpdatesWorkerRepository implements UpdatesWorkerRepository {

    @Autowired
    private UpdatesWorkerRepositoryConfiguration updatesWorkerRepositoryConfiguration;

    private BlockingQueue<UpdateEvent> updatesQueue;

    private UpdatesSaver updatesSaver;

    @Override
    @Timed(name = "bot.updates.worker.repository.save", absolute = true)
    public void save(@NotNull UpdateEvents updateEvents) {
        updatesSaver.save(updateEvents);
    }

    @Override
    @Timed(name = "bot.updates.worker.repository.get", absolute = true)
    public UpdateEvent get() {
        UpdateEvent updateEvent = updatesSaver.next().orElse(EMPTY);

        if (updateEvent != EMPTY) {
            log.trace("Returned non empty event {}", updateEvent);
        }
        return updateEvent;
    }

    @PostConstruct
    public void init() {
        updatesQueue = new ArrayBlockingQueue<>(updatesWorkerRepositoryConfiguration.getQueueSize());
        updatesSaver = new BlockingUpdatesSaver(updatesQueue);
    }
}
