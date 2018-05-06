package com.github.springtg.bot.platform.client.command.impl;

import org.telegram.telegrambots.api.methods.send.SendSticker;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Getter
@ToString
public class SendStickerCommand extends ApiMessageCommand<SendSticker> {
    public SendStickerCommand(SendSticker command) {
        super(command);
    }
}
