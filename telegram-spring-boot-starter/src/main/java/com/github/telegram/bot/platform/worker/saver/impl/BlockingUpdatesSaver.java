package com.github.telegram.bot.platform.worker.saver.impl;

import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.model.UpdateEvents;
import com.github.telegram.bot.platform.worker.saver.UpdatesSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
public class BlockingUpdatesSaver implements UpdatesSaver {
    private final Logger log = LoggerFactory.getLogger(BlockingUpdatesSaver.class);

    private final BlockingQueue<UpdateEvent> updatesQueue;

    BlockingUpdatesSaver(BlockingQueue<UpdateEvent> updatesQueue) {
        this.updatesQueue = updatesQueue;
    }

    @Override
    public void save(@NotNull UpdateEvents updateEvents) {
        updateEvents.getUpdateEventList().stream()
                .filter(Objects::nonNull)
                .forEach(updateEvent -> {
                    try {
                        updatesQueue.put(updateEvent);
                        log.trace("Saved update event [{}]", updateEvent);
                    } catch (InterruptedException e) {
                        log.error("Couldn't save update events", e);
                    }
                });
    }

    @Override
    public Optional<UpdateEvent> next() {
        try {
            UpdateEvent poll = updatesQueue.poll(500, MILLISECONDS);
            return ofNullable(poll);
        } catch (InterruptedException e) {
            log.debug("Can't take message from queue", e);
        }

        return empty();
    }
}

