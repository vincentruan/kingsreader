package com.github.springtg.bot.platform.client.impl;

import com.github.springtg.bot.platform.client.NextOffsetStrategy;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public class InMemoryNextOffsetStrategyImpl implements NextOffsetStrategy {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Integer getNextOffset() {
        return counter.get();
    }

    @Override
    public void saveLastOffset(@NotNull Integer lasOffset) {
        counter.set(lasOffset + 1);
    }
}
