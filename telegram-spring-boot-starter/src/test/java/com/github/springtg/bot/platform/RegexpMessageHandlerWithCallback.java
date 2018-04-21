package com.github.springtg.bot.platform;

import com.github.springtg.bot.platform.client.command.Reply;
import com.github.springtg.bot.platform.client.command.ReplyTo;
import com.github.springtg.bot.platform.handler.annotation.MessageHandler;
import com.github.springtg.bot.platform.handler.annotation.MessageMapping;
import com.github.springtg.bot.platform.model.UpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;

@MessageHandler
public class RegexpMessageHandlerWithCallback {

    public static final String MESSAGE_TEXT_PATTERN_REGEXP = ".*(hi|hello).*";
    public static final String MESSAGE_ANSWER = "Hi there";
    @Autowired
    private CallbackMethod callbackMethod;

    @MessageMapping(regexp = MESSAGE_TEXT_PATTERN_REGEXP)
    public Reply handle(UpdateEvent updateEvent) {
        return ReplyTo.to(updateEvent).withMessage(MESSAGE_ANSWER)
                .setCallback(message -> callbackMethod.run(message));
    }
}