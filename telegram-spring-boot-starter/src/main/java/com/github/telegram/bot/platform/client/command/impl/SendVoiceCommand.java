package com.github.telegram.bot.platform.client.command.impl;

import com.github.telegram.bot.platform.model.api.methods.send.SendVoice;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendVoiceCommand extends ApiMessageCommand<SendVoice> {
    public SendVoiceCommand(SendVoice command) {
        super(command);
    }
}
