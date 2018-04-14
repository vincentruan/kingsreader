package com.github.telegram.bot.platform.config;

import com.github.telegram.bot.platform.client.TelegramBotApi;
import com.github.telegram.bot.platform.client.impl.TelegramBotApiImpl;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Configuration
@Getter
public class UpdatesRepositoryConfiguration {

    @Value("${repository.client.pooling.timeout.sec:100}")
    private Integer poolingTimeout;

    @Value("${repository.client.pooling.limit:20}")
    private Integer poolingLimit;

    @Value("${telegram.client.maxConnectionsPerHost:1}")
    private Integer maxConnectionsPerHost;

    @Value("${telegram.client.maxRequestRetry:2}")
    private Integer maxRequestRetry;

    @Value("${telegram.client.readTimeout:60000}")
    private Integer readTimeout;

    @Value("${telegram.client.connectTimeout:10000}")
    private Integer connectTimeout;

    @Autowired
    private ClientProxyConfiguration clientProxyConfiguration;

    @Autowired
    private TelegramBotClientConfiguration telegramBotClientConfiguration;

    @Bean
    public EventBus getEventBus() {
        return new EventBus();
    }

    @Bean(name = "updateRepositoryBotApi")
    public TelegramBotApi telegramBotApi() {
        return new TelegramBotApiImpl(telegramBotClientConfiguration.createTelegramBotClient(getClient()));
    }

    private AsyncHttpClient getClient() {
        DefaultAsyncHttpClientConfig.Builder asyncHttpClientConfigBuilder = new DefaultAsyncHttpClientConfig.Builder()
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .setMaxRequestRetry(maxRequestRetry)
                .setMaxConnectionsPerHost(maxConnectionsPerHost)
                .setProxyServer(clientProxyConfiguration.getProxyServer());

        return new DefaultAsyncHttpClient(asyncHttpClientConfigBuilder.build());
    }
}
