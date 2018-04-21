package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.model.api.methods.send.SendPhoto;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendPhotoCommand extends ApiMessageCommand<SendPhoto> {
    public SendPhotoCommand(SendPhoto command) {
        super(command);
    }
}
