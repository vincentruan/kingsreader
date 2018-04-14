package com.github.telegram.bot.platform.handler.regexp;

import com.github.telegram.bot.platform.client.command.Reply;
import com.github.telegram.bot.platform.client.command.ReplyTo;
import com.github.telegram.bot.platform.handler.annotation.MessageHandler;
import com.github.telegram.bot.platform.handler.annotation.MessageMapping;
import com.github.telegram.bot.platform.model.UpdateEvent;

@MessageHandler
public class RegexpMessageHandler {

    public static final String MESSAGE_TEXT_PATTERN_REGEXP = ".*(hi|hello).*";
    public static final String MESSAGE_ANSWER = "Hi there";

    @MessageMapping(regexp = MESSAGE_TEXT_PATTERN_REGEXP)
    public Reply handle(UpdateEvent updateEvent) {
        return ReplyTo.to(updateEvent).withMessage(MESSAGE_ANSWER);
    }
}