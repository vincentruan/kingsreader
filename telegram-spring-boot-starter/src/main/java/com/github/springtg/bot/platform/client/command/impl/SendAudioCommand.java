package com.github.springtg.bot.platform.client.command.impl;

import org.telegram.telegrambots.api.methods.send.SendAudio;
import lombok.Getter;
import lombok.ToString;

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
