package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.client.TelegramBotApi;
import com.github.springtg.bot.platform.client.command.ApiCommand;
import com.github.springtg.bot.platform.client.command.ApiCommandSender;
import com.github.springtg.bot.platform.config.ApiCommandSenderConfiguration;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.github.springtg.bot.platform.client.command.ApiCommand.EMPTY_CALLBACK;
import static com.google.common.util.concurrent.MoreExecutors.shutdownAndAwaitTermination;

/**
 * @author Sergey Kuptsov
 * @since 01/06/2016
 */
@Component
@Slf4j
public class ApiCommandSenderImpl extends AbstractExecutionThreadService implements ApiCommandSender {

    @Autowired
    private ApiCommandSenderConfiguration apiCommandSenderConfiguration;

    @Autowired
    @Qualifier("commandSenderBotApi")
    private TelegramBotApi telegramBotApi;

    private BlockingQueue<ApiCommand<?>> apiCommandQueue;

    private ExecutorService apiCommandSenderExecutor;

    private ExecutorService apiCommandSenderCallbackExecutor;

    @Override
    public void sendCommand(ApiCommand apiCommand) {
        log.trace("Sending api command {}", apiCommand);
        try {
            apiCommandQueue.offer(apiCommand, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Can't send command {}", apiCommand);
        }
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            sendCommand();
        }
    }

    //todo: try to generify
    private void sendCommand() throws InterruptedException {
        ApiCommand apiCommand = apiCommandQueue.poll(100, TimeUnit.MILLISECONDS);

        if (apiCommand != null) {
            CompletableFuture<Future<?>> sendCommandFuture =
                    CompletableFuture.supplyAsync(() -> apiCommand.execute(telegramBotApi), apiCommandSenderExecutor);

            if (apiCommand.getCallback() != EMPTY_CALLBACK) {
                sendCommandFuture.thenAcceptAsync(
                        apiCommand::callback,
                        apiCommandSenderCallbackExecutor);
            }
        }
    }

    @Override
    protected void startUp() {
        apiCommandQueue = new ArrayBlockingQueue<>(apiCommandSenderConfiguration.getQueueSize());

        apiCommandSenderExecutor = Executors.newFixedThreadPool(
                apiCommandSenderConfiguration.getThreadCount(),
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("ApiCommandSenderTask-%d")
                        .build());

        apiCommandSenderCallbackExecutor = Executors.newFixedThreadPool(
                apiCommandSenderConfiguration.getThreadCount(),
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("ApiCommandSenderCallbackTask-%d")
                        .build());
    }

    @Override
    protected void triggerShutdown() {
        shutdownAndAwaitTermination(
                apiCommandSenderExecutor,
                2,
                TimeUnit.SECONDS);
        shutdownAndAwaitTermination(
                apiCommandSenderCallbackExecutor,
                2,
                TimeUnit.SECONDS);
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
