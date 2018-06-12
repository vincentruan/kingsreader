package com.github.springtg.bot.platform.config;

import com.github.springtg.bot.platform.client.TelegramBotApi;
import com.github.springtg.bot.platform.client.impl.TelegramBotApiImpl;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Kuptsov
 * @since 01/06/2016
 */
@Configuration
@Getter
public class ApiCommandSenderConfiguration {

    @Value("${api.command.sender.task.thread.corePoolSize:4}")
    private int corePoolSize;

    @Value("${api.command.sender.task.thread.maximumPoolSize:32}")
    private int maximumPoolSize;

    @Value("${api.command.sender.task.thread.keepAliveSeconds:60}")
    private int keepAliveSeconds;

    @Value("${api.command.sender.task.thread.queueCapacity:" + Integer.MAX_VALUE + "}")
    private int queueCapacity;

    @Value("${api.command.sender.task.queue.size:2048}")
    private int queueSize;

    @Value("${telegram.client.maxConnectionsPerHost:4}")
    private int maxConnectionsPerHost;

    @Value("${telegram.client.maxRequestRetry:2}")
    private int maxRequestRetry;

    @Value("${telegram.client.readTimeout:60000}")
    private int readTimeout;

    @Value("${telegram.client.connectTimeout:10000}")
    private int connectTimeout;

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
        DefaultAsyncHttpClientConfig.Builder asyncHttpClientConfigBuilder = Dsl.config()
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .setMaxRequestRetry(maxRequestRetry)
                .setMaxConnectionsPerHost(maxConnectionsPerHost)
                .setProxyServer(clientProxyConfiguration.getProxyServer());

        return new DefaultAsyncHttpClient(asyncHttpClientConfigBuilder.build());
    }
}
