package com.github.springtg.bot.platform.config;

import com.github.springtg.bot.platform.client.TelegramBotApi;
import com.github.springtg.bot.platform.client.impl.TelegramBotApiImpl;
import lombok.Getter;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Sergey Kuptsov
 * @since 01/06/2016
 */
@Configuration
@Getter
public class ApiCommandSenderConfiguration {

    @Value("${api.command.sender.task.thread.size:4}")
    private Integer threadCount;

    @Value("${api.command.sender.task.queue.size:2048}")
    private Integer queueSize;

    @Value("${telegram.client.maxConnectionsPerHost:4}")
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

    @Primary
    @Bean(name = "commandSenderBotApi")
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
