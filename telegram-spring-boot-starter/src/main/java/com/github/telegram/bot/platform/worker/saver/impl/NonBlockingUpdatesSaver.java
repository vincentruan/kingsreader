package com.github.telegram.bot.platform.worker.saver.impl;

import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.model.UpdateEvents;
import com.github.telegram.bot.platform.worker.saver.UpdatesSaver;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.sleep;
import static java.util.Optional.ofNullable;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
@Slf4j
public final class NonBlockingUpdatesSaver implements UpdatesSaver {

    private final BlockingQueue<UpdateEvent> updatesQueue;

    NonBlockingUpdatesSaver(BlockingQueue<UpdateEvent> updatesQueue) {
        this.updatesQueue = updatesQueue;
    }

    @Override
    public void save(@NotNull UpdateEvents updateEvents) {
        updateEvents.getUpdateEventList().stream()
                .filter(Objects::nonNull)
                .forEach(updateEvent -> {
                    boolean offered = updatesQueue.offer(updateEvent);
                    if (!offered) {
                        log.warn("UpdateEvent [{}] hasn't been saved, Queue is full", updateEvent);
                    } else {
                        log.trace("Saved update event [{}]", updateEvent);
                    }
                });
    }

    @Override
    public Optional<UpdateEvent> next() {
        Optional<UpdateEvent> updateEvent;
        while (!(updateEvent = ofNullable(updatesQueue.poll())).isPresent()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                log.error("Error", e);
            }
        }

        return updateEvent;
    }
}
