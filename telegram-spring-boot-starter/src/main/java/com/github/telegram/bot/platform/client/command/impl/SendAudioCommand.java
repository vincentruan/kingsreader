package com.github.telegram.bot.platform.client.command.impl;

import lombok.Getter;
import lombok.ToString;
import com.github.telegram.bot.platform.model.api.methods.send.SendAudio;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendAudioCommand extends ApiMessageCommand<SendAudio> {
    public SendAudioCommand(SendAudio command) {
        super(command);
    }
}
