package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.client.command.ApiCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Slf4j
public abstract class AbstractApiCommand<T> implements ApiCommand<T> {

    private Consumer<T> callback = EMPTY_CALLBACK;

    @Override
    public void callback(Future<T> future) {
        try {
            callback.accept(future.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Got execution exception", e);
        }
    }
}
