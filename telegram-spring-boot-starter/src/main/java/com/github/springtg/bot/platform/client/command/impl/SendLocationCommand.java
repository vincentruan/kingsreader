package com.github.springtg.bot.platform.client.command.impl;

import lombok.Getter;
import lombok.ToString;
import org.telegram.telegrambots.api.methods.send.SendLocation;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendLocationCommand extends ApiMessageCommand<SendLocation> {
    public SendLocationCommand(SendLocation command) {
        super(command);
    }
}
