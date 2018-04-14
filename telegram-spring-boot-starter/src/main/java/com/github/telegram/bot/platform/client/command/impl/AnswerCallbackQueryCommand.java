package com.github.telegram.bot.platform.client.command.impl;

import com.github.telegram.bot.platform.client.TelegramBotApi;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;

import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author Sergey Kuptsov
 * @since 25/08/2016
 */
public class AnswerCallbackQueryCommand extends AbstractApiCommand<Boolean> {

    private final AnswerCallbackQuery answerCallbackQuery;

    public AnswerCallbackQueryCommand(Consumer<Boolean> callback, AnswerCallbackQuery answerCallbackQuery) {
        super(callback);
        this.answerCallbackQuery = answerCallbackQuery;
    }

    @Override
    public Future<Boolean> execute(TelegramBotApi telegramBotApi) {
        return telegramBotApi.answerCallbackQuery(answerCallbackQuery).async();
    }
}
