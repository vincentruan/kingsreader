package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.model.api.methods.send.SendDocument;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendDocumentCommand extends ApiMessageCommand<SendDocument> {
    public SendDocumentCommand(SendDocument command) {
        super(command);
    }
}
