package com.github.springtg.bot.platform.client;

import com.fasterxml.jackson.databind.JavaType;
import com.github.springtg.bot.platform.client.exception.TelegramBotApiException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface TelegramBotHttpClient {

    /**
     * 异步调用http get
     * @param method 后缀，对应baseurl/{}
     * @param params get请求参数
     * @param returnType 返回类型
     * @param <T>
     * @return
     * @throws TelegramBotApiException
     */
    <T> Future<T> executeGet(@NotNull String method,
                             @Nullable Map<String, String> params,
                             @NotNull JavaType returnType) throws TelegramBotApiException;

    /**
     * 异步调用http get
     * @param method 后缀，对应baseurl/{}
     * @param requestObject post请求参数
     * @param returnType 返回类型
     * @param <T>
     * @return
     * @throws TelegramBotApiException
     */
    <T, V> Future<T> executePost(
            @NotNull String method,
            @Nullable V requestObject,
            @NotNull JavaType returnType) throws TelegramBotApiException;
}
