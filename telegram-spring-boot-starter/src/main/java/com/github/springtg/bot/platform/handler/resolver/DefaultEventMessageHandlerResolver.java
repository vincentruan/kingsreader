package com.github.springtg.bot.platform.handler.resolver;

import com.github.springtg.bot.platform.handler.DefaultMessageHandler;
import com.github.springtg.bot.platform.handler.MessageHandler;
import com.github.springtg.bot.platform.model.UpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@Component
public class DefaultEventMessageHandlerResolver extends AbstractMessageHandlerResolver {

    private final DefaultMessageHandler defaultEventProcessor;

    @Autowired
    public DefaultEventMessageHandlerResolver(@NotNull DefaultMessageHandler defaultEventProcessor) {
        this.defaultEventProcessor = defaultEventProcessor;
    }

    @Override
    protected MessageHandler resolveProcessor(UpdateEvent updateEvent) {
        return defaultEventProcessor;
    }
}
