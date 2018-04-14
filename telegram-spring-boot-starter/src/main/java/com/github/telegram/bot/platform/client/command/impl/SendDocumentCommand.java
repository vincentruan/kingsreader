package com.github.telegram.bot.platform.client.command.impl;

import lombok.Getter;
import lombok.ToString;
import com.github.telegram.bot.platform.model.api.methods.send.SendDocument;

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
