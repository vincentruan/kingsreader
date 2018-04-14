package com.github.telegram.bot.platform.handler.callback;

import com.github.telegram.bot.platform.CommonTestConfiguration;
import com.github.telegram.bot.platform.client.TelegramBotApi;
import com.github.telegram.bot.platform.common.BaseTestMatcher;
import com.github.telegram.bot.platform.config.BotPlatformConfiguration;
import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.model.UpdateEvents;
import com.github.telegram.bot.platform.repository.UpdatesRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.kubek2k.springockito.annotations.WrapWithSpy;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Date;
import java.util.Objects;

import static com.github.telegram.bot.platform.CommonTestConfiguration.CHAT_ID;
import static com.github.telegram.bot.platform.handler.callback.MessageCallbackProcessor.MESSAGE_ANSWER;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Sergey Kuptsov
 * @since 27/11/2016
 */
@ActiveProfiles({"test"})
@ContextConfiguration(
        classes = {
                BotPlatformConfiguration.class,
                CommonTestConfiguration.class,
                MessageCallbackProcessor.class,
        },
        loader = SpringockitoAnnotatedContextLoader.class
)
public class MessageCallbackDataHandlerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    @ReplaceWithMock
    private UpdatesRepository updatesRepository;

    @Autowired
    private EventBus eventBus;

    @Autowired
    @Qualifier("commandSenderBotApi")
    @WrapWithSpy
    private TelegramBotApi telegramBotApi;

    @Before
    public void before() {
        Mockito.reset(telegramBotApi);
    }

    @Test
    public void answerSent() throws InterruptedException {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        CallbackQuery callbackQuery = Mockito.mock(CallbackQuery.class);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn("1");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(update.getMessage()).thenReturn(message);

        UpdateEvent updateEvent = new UpdateEvent(update, new Date());

        eventBus.post(UpdateEvents.builder()
                .updateEventList(ImmutableList.of(updateEvent))
                .build());

        //await termination
        Thread.sleep(1000);

        SendMessage answer = new SendMessage();
        answer.setChatId(CHAT_ID.toString());
        answer.setText(MESSAGE_ANSWER);

        verify(telegramBotApi)
                .sendMessage(argThat(new SendMessageObjectMatcher(answer)));
    }

    @Test
    public void answerNotSent() throws InterruptedException {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        CallbackQuery callbackQuery = Mockito.mock(CallbackQuery.class);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn("2");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(update.getMessage()).thenReturn(message);

        UpdateEvent updateEvent = new UpdateEvent(update, new Date());

        eventBus.post(UpdateEvents.builder()
                .updateEventList(ImmutableList.of(updateEvent))
                .build());

        //await termination
        Thread.sleep(1000);

        SendMessage callbackMessage = new SendMessage();
        callbackMessage.setChatId(CHAT_ID.toString());
        callbackMessage.setText(MESSAGE_ANSWER);

        verify(telegramBotApi, never())
                .sendMessage(argThat(new SendMessageObjectMatcher(callbackMessage)));
    }

    private static class SendMessageObjectMatcher extends BaseTestMatcher<SendMessage> {

        public SendMessageObjectMatcher(SendMessage object) {
            super(object);
        }

        @Override
        protected boolean isEqual(SendMessage that) {
            return Objects.equals(that.getMethod(), expected.getMethod()) &&
                    Objects.equals(that.getText(), expected.getText()) &&
                    Objects.equals(that.getChatId(), expected.getChatId());
        }
    }
}
