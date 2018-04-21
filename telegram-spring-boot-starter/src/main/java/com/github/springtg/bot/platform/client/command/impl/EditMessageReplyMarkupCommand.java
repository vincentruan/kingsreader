package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.model.updatingmessages.EditMessageReplyMarkup;
import lombok.Getter;
import lombok.ToString;

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
