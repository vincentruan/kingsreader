package com.github.springtg.bot.platform.client.command.impl;

import com.github.springtg.bot.platform.client.TelegramBotApi;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;

import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author Sergey Kuptsov
 * @since 25/08/2016
 */
public class AnswerInlineQueryCommand extends AbstractApiCommand<Boolean> {

    private final AnswerInlineQuery answerInlineQuery;

    public AnswerInlineQueryCommand(Consumer<Boolean> callback, AnswerInlineQuery answerInlineQuery) {
        super(callback);
        this.answerInlineQuery = answerInlineQuery;
    }

    @Override
    public Future<Boolean> execute(TelegramBotApi telegramBotApi) {
        return telegramBotApi.answerInlineQuery(answerInlineQuery).async();
    }
}
