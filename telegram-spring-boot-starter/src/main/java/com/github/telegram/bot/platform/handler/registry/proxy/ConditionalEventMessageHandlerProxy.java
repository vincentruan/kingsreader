package com.github.telegram.bot.platform.handler.registry.proxy;

import com.google.common.base.Preconditions;
import com.github.telegram.bot.platform.handler.ConditionMessageHandler;
import com.github.telegram.bot.platform.handler.annotation.MessageFilter;
import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.service.MetricsService;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 24/07/2016
 */
public class ConditionalEventMessageHandlerProxy extends BaseMessageHandlerProxy implements ConditionMessageHandler {

    private final MessageFilter messageFilter;

    public ConditionalEventMessageHandlerProxy(HandlerMethod handlerMethod, @NotNull MessageFilter messageFilter, MetricsService metricsService) {
        super(handlerMethod, metricsService);
        Preconditions.checkNotNull(messageFilter, "Message filter cannot be null");
        this.messageFilter = messageFilter;
    }

    @Override
    public boolean isSuitableForProcessingEvent(@NotNull UpdateEvent updateEvent) {
        return messageFilter.test(updateEvent);
    }
}
