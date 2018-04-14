package com.github.telegram.bot.platform.handler;

import com.github.telegram.bot.platform.client.command.Reply;
import com.github.telegram.bot.platform.model.UpdateEvent;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface MessageHandler {
    MessageHandler EMPTY = (updateEvent) -> Reply.EMPTY;

    Reply handle(UpdateEvent updateEvent);
}
