package com.github.springtg.bot.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Date;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEvent {
    public final static UpdateEvent EMPTY = new UpdateEvent();

    /**
     * Original update receive from telegram
     */
    private Update update;
    /**
     * Received time
     */
    private Date received;
}
