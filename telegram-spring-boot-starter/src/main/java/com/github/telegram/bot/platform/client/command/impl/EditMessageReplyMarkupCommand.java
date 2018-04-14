package com.github.telegram.bot.platform.client.command.impl;

import lombok.Getter;
import lombok.ToString;
import com.github.telegram.bot.platform.model.updatingmessages.EditMessageReplyMarkup;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class EditMessageReplyMarkupCommand extends ApiMessageCommand<EditMessageReplyMarkup> {
    public EditMessageReplyMarkupCommand(EditMessageReplyMarkup message) {
        super(message);
    }
}
