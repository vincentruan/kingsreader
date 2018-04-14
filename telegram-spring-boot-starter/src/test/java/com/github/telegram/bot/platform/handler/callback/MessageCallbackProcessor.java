package com.github.telegram.bot.platform.handler.callback;

import com.github.telegram.bot.platform.client.command.Reply;
import com.github.telegram.bot.platform.client.command.ReplyTo;
import com.github.telegram.bot.platform.handler.annotation.MessageHandler;
import com.github.telegram.bot.platform.handler.annotation.MessageMapping;
import com.github.telegram.bot.platform.model.UpdateEvent;

@MessageHandler
public class MessageCallbackProcessor {

    public static final String MESSAGE_SELECTED = "1";
    public static final String MESSAGE_ANSWER = "OK";

    @MessageMapping(callback = MESSAGE_SELECTED)
    public Reply handle(UpdateEvent updateEvent) {
        return ReplyTo.to(updateEvent).withMessage(MESSAGE_ANSWER);
    }
}