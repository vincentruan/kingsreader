package com.github.springtg.bot.platform.client.command.impl;

import org.telegram.telegrambots.api.methods.send.SendVideo;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendVideoCommand extends ApiMessageCommand<SendVideo> {
    public SendVideoCommand(SendVideo command) {
        super(command);
    }
}
