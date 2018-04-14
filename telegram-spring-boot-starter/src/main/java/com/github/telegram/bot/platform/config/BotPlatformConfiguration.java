package com.github.telegram.bot.platform.config;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * @author Sergey Kuptsov
 * @since 21/05/2016
 */
@Configuration
@Import(value = {
        UpdatesRepositoryConfiguration.class,
        TelegramBotClientConfiguration.class,
        UpdatesWorkerRepositoryConfiguration.class,
        WorkerConfiguration.class,
        ApiCommandSenderConfiguration.class
})
@ComponentScan(value = "com.github.telegram.bot")
@ConditionalOnClass(TelegramBotsApi.class)
public class BotPlatformConfiguration {

    static {
        // SLF4JBridgeHandler.install();
        BotLogger.registerLogger(new SLF4JBridgeHandler());
        ApiContextInitializer.init();
    }

}
