package com.github.telegram.bot.platform.handler.registry.proxy;

import com.github.telegram.bot.platform.handler.CallbackQueryDataMessageHandler;
import com.github.telegram.bot.platform.service.MetricsService;

import java.util.Set;

/**
 * @author Sergey Kuptsov
 * @since 23/07/2016
 */
public class CallbackQueryDataMessageProxy extends BaseMessageHandlerProxy implements CallbackQueryDataMessageHandler {

    private final Set<String> callbackQueryData;

    public CallbackQueryDataMessageProxy(MetricsService metricsService, HandlerMethod handlerMethod, Set<String> callbackQueryData) {
        super(handlerMethod, metricsService);
        this.callbackQueryData = callbackQueryData;
    }

    @Override
    public Set<String> getCallbackQueryData() {
        return callbackQueryData;
    }
}
