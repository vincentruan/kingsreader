package com.github.springtg.bot.platform.client.command;

import com.github.springtg.bot.platform.client.command.impl.AnswerCallbackQueryCommand;
import com.github.springtg.bot.platform.client.command.impl.AnswerInlineQueryCommand;
import com.github.springtg.bot.platform.client.command.impl.EditMessageCaptionCommand;
import com.github.springtg.bot.platform.client.command.impl.EditMessageReplyMarkupCommand;
import com.github.springtg.bot.platform.client.command.impl.EditMessageTextCommand;
import com.github.springtg.bot.platform.client.command.impl.ForwardMessageCommand;
import com.github.springtg.bot.platform.client.command.impl.SendAudioCommand;
import com.github.springtg.bot.platform.client.command.impl.SendContactCommand;
import com.github.springtg.bot.platform.client.command.impl.SendDocumentCommand;
import com.github.springtg.bot.platform.client.command.impl.SendLocationCommand;
import com.github.springtg.bot.platform.client.command.impl.SendMessageCommand;
import com.github.springtg.bot.platform.client.command.impl.SendPhotoCommand;
import com.github.springtg.bot.platform.client.command.impl.SendStickerCommand;
import com.github.springtg.bot.platform.client.command.impl.SendVenueCommand;
import com.github.springtg.bot.platform.client.command.impl.SendVideoCommand;
import com.github.springtg.bot.platform.client.command.impl.SendVoiceCommand;
import com.github.springtg.bot.platform.model.UpdateEvent;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import com.github.springtg.bot.platform.model.updatingmessages.EditMessageCaption;
import com.github.springtg.bot.platform.model.updatingmessages.EditMessageReplyMarkup;
import com.github.springtg.bot.platform.model.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.ForwardMessage;
import org.telegram.telegrambots.api.methods.send.SendContact;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.objects.Message;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * @author Sergey Kuptsov
 * @since 24/07/2016
 * <p>
 * Message reply to answer on chat activity
 */
public class Reply<T> {
    public final static Reply EMPTY = new Reply<>(ApiCommand.EMPTY);

    private ApiCommand<T> apiCommand;

    private Reply(ApiCommand<T> apiCommand) {
        this.apiCommand = apiCommand;
    }

    private static <T> Reply<T> withCommand(@NotNull ApiCommand<T> apiCommand) {
        return new Reply<>(apiCommand);
    }

    public static Reply<Message> withMessage(@NotNull SendMessage sendMessage) {
        return withCommand(new SendMessageCommand(sendMessage));
    }

    public static Reply<Message> withEditMessageText(@NotNull EditMessageText editMessageText) {
        return withCommand(new EditMessageTextCommand(editMessageText));
    }

    public static Reply<Message> withEditMessageReplyMarkup(@NotNull EditMessageReplyMarkup editMessageReplyMarkup) {
        return withCommand(new EditMessageReplyMarkupCommand(editMessageReplyMarkup));
    }

    public static Reply<Message> withContact(@NotNull SendContact sendContact) {
        return withCommand(new SendContactCommand(sendContact));
    }

    public static Reply<Message> withLocation(@NotNull SendLocation message) {
        return withCommand(new SendLocationCommand(message));
    }

    public static Reply<Message> withDocument(@NotNull SendDocument message) {
        return withCommand(new SendDocumentCommand(message));
    }

    public static Reply<Message> withPhoto(@NotNull SendPhoto message) {
        return withCommand(new SendPhotoCommand(message));
    }

    public static Reply<Message> withSticker(@NotNull SendSticker message) {
        return withCommand(new SendStickerCommand(message));
    }

    public static Reply<Message> withVideo(@NotNull SendVideo message) {
        return withCommand(new SendVideoCommand(message));
    }

    public static Reply<Message> withVoice(@NotNull SendVoice message) {
        return withCommand(new SendVoiceCommand(message));
    }

    public static Reply<Message> withAudio(@NotNull SendAudio message) {
        return withCommand(new SendAudioCommand(message));
    }

    public static Reply<Boolean> answerCallbackQuery(@NotNull AnswerCallbackQuery message) {
        return withCommand(new AnswerCallbackQueryCommand(null, message));
    }

    public static Reply<Boolean> answerInlineQuery(@NotNull AnswerInlineQuery message) {
        return withCommand(new AnswerInlineQueryCommand(null, message));
    }

    public static Reply<Message> withVenue(@NotNull SendVenue message) {
        return withCommand(new SendVenueCommand(message));
    }

    public static Reply<Message> editMessageCaption(@NotNull EditMessageCaption message) {
        return withCommand(new EditMessageCaptionCommand(message));
    }

    public static Reply<Message> withForwardMessage(@NotNull ForwardMessage message) {
        return withCommand(new ForwardMessageCommand(message));
    }

    private static String getChatId(UpdateEvent updateEvent) {
        return updateEvent.getUpdate().getMessage().getChatId().toString();
    }

    public ApiCommand<T> getApiCommand() {
        return apiCommand;
    }

    public Reply<T> setCallback(Consumer<T> callback) {
        apiCommand.setCallback(callback);

        return this;
    }
}