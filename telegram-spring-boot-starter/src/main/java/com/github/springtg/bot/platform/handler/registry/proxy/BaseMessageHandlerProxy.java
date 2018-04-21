package com.github.springtg.bot.platform.handler.registry.proxy;

import com.github.springtg.bot.platform.client.command.Reply;
import com.github.springtg.bot.platform.client.exception.HandlerMethodInvocationException;
import com.github.springtg.bot.platform.client.exception.TelegramBotApiException;
import com.github.springtg.bot.platform.handler.MessageHandler;
import com.github.springtg.bot.platform.model.UpdateEvent;
import com.github.springtg.bot.platform.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Sergey Kuptsov
 * @since 24/07/2016
 */
@Slf4j
public abstract class BaseMessageHandlerProxy implements MessageHandler {

    private final HandlerMethod handlerMethod;

    private final MetricsService metricsService;

    public BaseMessageHandlerProxy(HandlerMethod handlerMethod, MetricsService metricsService) {
        this.handlerMethod = handlerMethod;
        this.metricsService = metricsService;
    }

    @Override
    public Reply handle(UpdateEvent updateEvent) {
        log.trace("Invoking handler [{}] with update event [{}]", this.getClass(), updateEvent);
        Method handlerMethodToInvoke = handlerMethod.getBridgedMethod();

        try {
            ReflectionUtils.makeAccessible(handlerMethodToInvoke);
            Object[] args = {updateEvent};

            long start = System.currentTimeMillis();

            Reply reply = (Reply) handlerMethodToInvoke.invoke(handlerMethod.getBean(), args);

            metricsService.onMessageProcessingComplete(handlerMethod, System.currentTimeMillis() - start);
            return reply;
        } catch (IllegalStateException ex) {
            throw new HandlerMethodInvocationException(handlerMethodToInvoke, ex);
        } catch (Exception ex) {
            metricsService.onMessageProcessingError(handlerMethod);
            throw new TelegramBotApiException(ex);
        }
    }
}
