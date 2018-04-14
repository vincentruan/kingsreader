package com.github.telegram.bot.platform.client.command.impl;

import lombok.Getter;
import lombok.ToString;
import com.github.telegram.bot.platform.model.updatingmessages.EditMessageText;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class EditMessageTextCommand extends ApiMessageCommand<EditMessageText> {
    public EditMessageTextCommand(EditMessageText message) {
        super(message);
    }
}
