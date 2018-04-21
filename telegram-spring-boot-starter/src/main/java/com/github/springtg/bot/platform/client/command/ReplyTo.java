package com.github.springtg.bot.platform.client.command;

import com.github.springtg.bot.platform.model.UpdateEvent;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import javax.validation.constraints.NotNull;

public class ReplyTo {

    private final String chatId;

    private ReplyTo(String chatId) {
        this.chatId = chatId;
    }

    public static ReplyTo to(@NotNull UpdateEvent updateEvent) {
        return new ReplyTo(updateEvent.getUpdate().getMessage().getChatId().toString());
    }

    public static ReplyTo to(@NotNull String chatId) {
        return new ReplyTo(chatId);
    }

    public Reply<Message> withMessage(@NotNull String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);

        return Reply.withMessage(sendMessage);
    }
};
