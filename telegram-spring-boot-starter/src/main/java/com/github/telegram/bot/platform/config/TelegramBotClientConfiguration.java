package com.github.telegram.bot.platform.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.telegram.bot.platform.client.TelegramBotHttpClient;
import com.github.telegram.bot.platform.client.impl.TelegramBotHttpClientImpl;
import org.asynchttpclient.AsyncHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Configuration
public class TelegramBotClientConfiguration {

    public static final String TELEGRAM_CLIENT_TOKEN = "telegram.client.token";

    @Value("${" + TELEGRAM_CLIENT_TOKEN + "}")
    private String clientToken;

    @Value("${telegram.client.baseUrl:https://api.telegram.org}")
    private String baseUrl;

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }

    public TelegramBotHttpClient createTelegramBotClient(AsyncHttpClient asyncHttpClient) {
        return new TelegramBotHttpClientImpl(
                getObjectMapper(),
                asyncHttpClient,
                clientToken,
                baseUrl);
    }
}
