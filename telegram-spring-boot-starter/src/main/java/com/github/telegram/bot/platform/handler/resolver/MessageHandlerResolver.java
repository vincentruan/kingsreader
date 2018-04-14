package com.github.telegram.bot.platform.handler.resolver;

import com.github.telegram.bot.platform.handler.MessageHandler;
import com.github.telegram.bot.platform.model.UpdateEvent;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public interface MessageHandlerResolver {
    MessageHandler resolve(@NotNull UpdateEvent updateEvent);

    MessageHandlerResolver setNext(MessageHandlerResolver messageHandlerResolver);
}
