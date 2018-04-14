package com.github.telegram.bot.platform.worker;

import com.github.telegram.bot.platform.client.command.ApiCommandSender;
import com.github.telegram.bot.platform.config.WorkerConfiguration;
import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.model.UpdateEvents;
import com.github.telegram.bot.platform.worker.saver.UpdatesWorkerRepository;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.telegram.bot.platform.model.UpdateEvent.EMPTY;
import static com.google.common.util.concurrent.MoreExecutors.shutdownAndAwaitTermination;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Component
public class UpdatesWorker extends AbstractExecutionThreadService {
    private final static Logger logger = LoggerFactory.getLogger(UpdatesWorker.class);

    @Autowired
    private EventBus eventBus;

    @Autowired
    private UpdatesWorkerRepository updatesWorkerRepository;

    @Autowired
    private WorkerConfiguration workerConfiguration;

    @Autowired
    private WorkerTaskFactory workerTaskFactory;

    @Autowired
    private ApiCommandSender apiCommandSender;

    private ExecutorService updatesWorkerExecutor;

    private ExecutorService apiCommandSenderExecutor;

    @AllowConcurrentEvents
    @Subscribe
    public void handleUpdateEvents(@NotNull UpdateEvents updateEvents) {
        logger.debug("Received event {}", updateEvents);
        updatesWorkerRepository.save(updateEvents);
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            UpdateEvent updateEvent = updatesWorkerRepository.get();
            if (updateEvent != EMPTY) {
                CompletableFuture.supplyAsync(
                        () -> workerTaskFactory.createFor(updateEvent).execute(),
                        updatesWorkerExecutor)
                        .thenAcceptAsync(
                                apiCommand -> apiCommandSender.sendCommand(apiCommand),
                                apiCommandSenderExecutor);
            }
        }
    }

    @Override
    protected void startUp() {
        eventBus.register(this);

        updatesWorkerExecutor = new ThreadPoolExecutor(workerConfiguration.getThreadCount(), workerConfiguration.getThreadCount(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("UpdatesWorkerTask-%d")
                        .build());


        apiCommandSenderExecutor = Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("ApiCommandSenderTask-%d")
                        .build());
    }

    @Override
    protected void triggerShutdown() {
        shutdownAndAwaitTermination(
                updatesWorkerExecutor,
                workerConfiguration.getShutdownTimeout(),
                workerConfiguration.getShutdownTimeoutTimeUnit());
        shutdownAndAwaitTermination(apiCommandSenderExecutor, 5, TimeUnit.SECONDS);
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
