package com.github.telegram.bot.platform.config;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.github.telegram.bot.platform.client.TelegramBotApi;
import com.github.telegram.bot.platform.client.impl.TelegramBotApiImpl;

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

    @Value("${telegram.client.allowPoolingConnections:true}")
    private Boolean allowPoolingConnections;

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
        AsyncHttpClientConfig.Builder asyncHttpClientConfigBuilder = new AsyncHttpClientConfig.Builder()
                .setAllowPoolingConnections(allowPoolingConnections)
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .setMaxRequestRetry(maxRequestRetry)
                .setMaxConnectionsPerHost(maxConnectionsPerHost)
                .setProxyServer(clientProxyConfiguration.getProxyServer());

        return new AsyncHttpClient(asyncHttpClientConfigBuilder
                .build());
    }
}
