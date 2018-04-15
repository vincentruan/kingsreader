package com.github.telegram.bot.platform.worker;

import com.codahale.metrics.annotation.Timed;
import com.github.telegram.bot.platform.client.command.ApiCommand;
import com.github.telegram.bot.platform.handler.MessageHandler;
import com.github.telegram.bot.platform.handler.resolver.EventMessageHandlerResolver;
import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
@Component
public class WorkerTaskFactory {

    @Autowired
    private EventMessageHandlerResolver eventProcessorResolver;

    @Autowired
    private MetricsService metricsService;

    public WorkerTask createFor(UpdateEvent updateEvent) {
        return new WorkerTask(updateEvent, eventProcessorResolver);
    }

    public final class WorkerTask {
        private final Logger log = LoggerFactory.getLogger(WorkerTask.class);

        private final UpdateEvent updateEvent;
        private final EventMessageHandlerResolver eventProcessorResolver;

        private WorkerTask(UpdateEvent updateEvent, EventMessageHandlerResolver eventProcessorResolver) {
            this.updateEvent = updateEvent;
            this.eventProcessorResolver = eventProcessorResolver;
        }

        public ApiCommand execute() {
            log.debug("Processing event {}", updateEvent);

            if (updateEvent == UpdateEvent.EMPTY) {
                log.warn("Event is empty");
                return ApiCommand.EMPTY;
            }

            return processEvent(updateEvent);
        }

        @Timed(name = "worker.task.handle.event", absolute = true)
        private ApiCommand processEvent(UpdateEvent updateEvent) {
            try {
                MessageHandler messageHandler = eventProcessorResolver.resolve(updateEvent);
                return messageHandler.handle(updateEvent).getApiCommand();
            } catch (Exception ex) {
                metricsService.onMessageProcessingError();
                log.error("Platform error occurred while processing event {}", updateEvent, ex);
            }

            return ApiCommand.EMPTY;
        }
    }
}
