package com.github.springtg.bot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.generics.LongPollingBot;
import org.telegram.telegrambots.generics.WebhookBot;
import org.telegram.telegrambots.logging.BotLogger;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives all beand which are #LongPollingBot and #WebhookBot and register them in #TelegramBotsApi.
 * #TelegramBotsApi added to spring context as well
 */
@Configuration
@ConditionalOnClass(TelegramBotsApi.class)
@Slf4j
public class TelegramBotStarterConfiguration implements CommandLineRunner {


    private final List<LongPollingBot> longPollingBots;

    private final List<WebhookBot> webHookBots;

    @Getter
    @Setter
    private String externalUrl = null;

    @Getter
    @Setter
    private String internalUrl = null;

    @Getter
    @Setter
    private String keyStore = null;

    @Getter
    @Setter
    private String keyStorePassword;

    @Getter
    @Setter
    private String pathToCertificate;


    private List<BotSession> sessions = new ArrayList<>();

    static {
        // SLF4JBridgeHandler.install();
        BotLogger.registerLogger(new SLF4JBridgeHandler());
        ApiContextInitializer.init();
    }

    @Autowired
    private TelegramBotsApi telegramBotsApi;

    public TelegramBotStarterConfiguration(List<LongPollingBot> longPollingBots,
                                           List<WebhookBot> webHookBots) {
        this.longPollingBots = longPollingBots;
        this.webHookBots = webHookBots;
    }

    @Override
    public void run(String... args) throws TelegramApiRequestException {
        log.info("Starting auto config for telegram bots");
        TelegramBotsApi api = telegramBotsApi();
        longPollingBots.forEach(bot -> {
            try {
                log.info("Registering polling bot: {}", bot.getBotUsername());
                sessions.add(api.registerBot(bot));
            } catch (TelegramApiException e) {
                log.error("Failed to register bot " + bot.getBotUsername() + " due to error", e);
            }
        });
        webHookBots.forEach(bot -> {
            try {
                log.info("Registering web hook bot: {}", bot.getBotUsername());
                api.registerBot(bot);
            } catch (TelegramApiException e) {
                log.error("Failed to register bot {} due to error {}", bot.getBotUsername(), e.getMessage());
            }
        });
    }


    @Bean
    @ConditionalOnMissingBean(TelegramBotsApi.class)
    public TelegramBotsApi telegramBotsApi() throws TelegramApiRequestException {
        TelegramBotsApi result;
        if (!StringUtils.isEmpty(externalUrl) && !StringUtils.isEmpty(internalUrl)) {
            if (!StringUtils.isEmpty(keyStore) && !StringUtils.isEmpty(keyStorePassword)) {
                if (!StringUtils.isEmpty(pathToCertificate)) {
                    log.info("Initializing API with webhook support and configured keystore and path to certificate");
                    result = new TelegramBotsApi(keyStore, keyStorePassword, externalUrl, internalUrl, pathToCertificate);
                } else {
                    log.info("Initializing API with webhook support and configured keystore");
                    result = new TelegramBotsApi(keyStore, keyStorePassword, externalUrl, internalUrl);
                }
            } else {
                log.info("Initializing API with webhook support");
                result = new TelegramBotsApi(externalUrl, internalUrl);
            }
        } else {
            log.info("Initializing API without webhook support");
            result = new TelegramBotsApi();
        }
        return result;
    }

    @PreDestroy
    public void stop() {
        sessions.forEach(session -> {
            if (session != null) {
                session.stop();
            }
        });
    }
}
