package com.github.telegram.bot.platform.client.impl;

import com.github.telegram.bot.platform.client.NextOffsetStrategy;
import com.github.telegram.bot.platform.client.TelegramBotApi;
import com.github.telegram.bot.platform.client.TelegramBotHttpClient;
import com.github.telegram.bot.platform.client.command.Reply;
import com.github.telegram.bot.platform.client.exception.TelegramBotApiException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.GetMe;
import org.telegram.telegrambots.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.UserProfilePhotos;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.github.telegram.bot.platform.client.utils.JavaTypeUtils.listTypeOf;
import static com.github.telegram.bot.platform.client.utils.JavaTypeUtils.simpleTypeOf;
import static com.google.common.collect.ImmutableMap.of;
import static java.util.Comparator.comparingInt;
import static org.telegram.telegrambots.api.methods.send.SendSticker.CHATID_FIELD;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Slf4j
public class TelegramBotApiImpl implements TelegramBotApi {
    private static final String FILEID_FIELD = "file_id";

    private final TelegramBotHttpClient client;

    @Autowired
    private NextOffsetStrategy nextOffsetStrategy;

    public TelegramBotApiImpl(TelegramBotHttpClient client) {
        this.client = client;
    }

    @Override
    public List<Update> getNextUpdates(Integer poolingLimit, Integer poolingTimeout) {
        List<Update> updates = new ArrayList<>();

        try {
            Future<List<Update>> futureUpdates = client.executeGet(
                    "getUpdates",
                    of("offset", nextOffsetStrategy.getNextOffset().toString(),
                            "timeout", poolingTimeout.toString(),
                            "limit", poolingLimit.toString()),
                    listTypeOf(Update.class));

            updates = futureUpdates.get();

            updates.stream()
                    .map(Update::getUpdateId)
                    .max(comparingInt(Integer::intValue).reversed())
                    .ifPresent(value ->
                            nextOffsetStrategy.saveLastOffset(value));
        } catch (InterruptedException | ExecutionException e) {
            log.error("Can't get updates with exception {}", e);
        }

        return updates;
    }

    @Override
    public <R extends BotApiMethod<Message>> CommandExecutionWrapper<Message> sendMessage(R message) {
        return CommandExecutionWrapper.from(client.executePost(
                message.getMethod(),
                message,
                simpleTypeOf(Message.class)
        ));
    }

    @Override
    public <T> CommandExecutionWrapper<T> reply(Reply<T> reply) {
        return CommandExecutionWrapper.from(reply.getApiCommand().execute(this));
    }

    @Override
    public CommandExecutionWrapper<User> getMe() {
        return CommandExecutionWrapper.from(client.executeGet(
                GetMe.PATH,
                null,
                simpleTypeOf(User.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<File> getFile(@NotNull String fileId) {
        return CommandExecutionWrapper.from(client.executeGet(
                GetFile.PATH,
                of(FILEID_FIELD, fileId),
                simpleTypeOf(File.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<UserProfilePhotos> getUserProfilePhotos(@NotNull GetUserProfilePhotos getUserProfilePhotos) {
        return CommandExecutionWrapper.from(client.executePost(
                GetUserProfilePhotos.PATH,
                getUserProfilePhotos,
                simpleTypeOf(UserProfilePhotos.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Boolean> answerInlineQuery(@NotNull AnswerInlineQuery answerInlineQuery) {
        return CommandExecutionWrapper.from(client.executePost(
                AnswerInlineQuery.PATH,
                answerInlineQuery,
                simpleTypeOf(Boolean.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Boolean> answerCallbackQuery(@NotNull AnswerCallbackQuery answerCallbackQuery) {
        return CommandExecutionWrapper.from(client.executePost(
                AnswerCallbackQuery.PATH,
                answerCallbackQuery,
                simpleTypeOf(Boolean.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Chat> getChat(@NotNull String chatId) {
        return CommandExecutionWrapper.from(client.executeGet(
                GetChat.PATH,
                of(CHATID_FIELD, chatId),
                simpleTypeOf(Chat.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<ArrayList<ChatMember>> getChatAdministrators(@NotNull String chatId) {
        return CommandExecutionWrapper.from(client.executeGet(
                GetChatAdministrators.PATH,
                of(CHATID_FIELD, chatId),
                listTypeOf(ChatMember.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Boolean> leaveChat(@NotNull String chatId) {
        return CommandExecutionWrapper.from(client.executeGet(
                LeaveChat.PATH,
                of(CHATID_FIELD, chatId),
                simpleTypeOf(Boolean.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Boolean> unbanChatMember(@NotNull String chatId, @NotNull Integer userId) {
        return CommandExecutionWrapper.from(client.executeGet(
                UnbanChatMember.PATH,
                of(CHATID_FIELD, chatId, "user_id", userId.toString()),
                simpleTypeOf(Boolean.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Boolean> kickChatMember(@NotNull String chatId, @NotNull Integer userId) {
        return CommandExecutionWrapper.from(client.executeGet(
                KickChatMember.PATH,
                of(CHATID_FIELD, chatId, "user_id", userId.toString()),
                simpleTypeOf(Boolean.class)
        ));
    }

    @Override
    public CommandExecutionWrapper<Integer> getChatMemberCount(@NotNull String chatId) {
        return CommandExecutionWrapper.from(client.executeGet(
                GetChatMemberCount.PATH,
                of(CHATID_FIELD, chatId),
                simpleTypeOf(Integer.class)
        ));
    }

    public static class CommandExecutionWrapper<T> {
        private final Future<T> result;

        private CommandExecutionWrapper(Future<T> result) {
            this.result = result;
        }

        public static <T> CommandExecutionWrapper<T> from(Future<T> result) {
            return new CommandExecutionWrapper<>(result);
        }

        public Future<T> async() {
            return result;
        }

        public T get() {
            try {
                return result.get();
            } catch (Throwable e) {
                throw new TelegramBotApiException("Error occured while executing method ", e);
            }
        }
    }
}
