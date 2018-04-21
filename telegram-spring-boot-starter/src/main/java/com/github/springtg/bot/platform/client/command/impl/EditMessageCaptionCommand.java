package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.model.updatingmessages.EditMessageCaption;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class EditMessageCaptionCommand extends ApiMessageCommand<EditMessageCaption> {
    public EditMessageCaptionCommand(EditMessageCaption command) {
        super(command);
    }
}
