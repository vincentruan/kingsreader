package com.github.springtg.bot.platform.handler.message;

import com.github.springtg.bot.platform.client.command.Reply;
import com.github.springtg.bot.platform.client.command.ReplyTo;
import com.github.springtg.bot.platform.handler.annotation.MessageHandler;
import com.github.springtg.bot.platform.handler.annotation.MessageMapping;
import com.github.springtg.bot.platform.model.UpdateEvent;

@MessageHandler
public class MessageTextHandler {

    public static final String MESSAGE_TEXT = "Bye";
    public static final String MESSAGE_ANSWER = "Goodbye";

    @MessageMapping(text = MESSAGE_TEXT)
    public Reply handle(UpdateEvent updateEvent) {
        return ReplyTo.to(updateEvent).withMessage(MESSAGE_ANSWER);
    }
}