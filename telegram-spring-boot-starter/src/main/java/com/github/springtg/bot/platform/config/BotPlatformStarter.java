package com.github.springtg.bot.platform.config;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.github.springtg.bot.platform.config.TelegramBotClientConfiguration.TELEGRAM_CLIENT_TOKEN;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.springframework.boot.SpringApplication.run;

/**
 * @author Sergey Kuptsov
 * @since 04/07/2016
 */
@Slf4j
public class BotPlatformStarter {
    private static final Class<BotPlatformConfiguration> BOT_PLATFORM_CONFIGURATION_CLASS = BotPlatformConfiguration.class;
    private static final List<Class<?>> configs = newArrayList(BOT_PLATFORM_CONFIGURATION_CLASS);

    public static void start(@NotNull List<Class<?>> configurations, @NotNull String[] args) {
        checkNotNull(configurations, "Configuration list cannot be null");
        checkArgument(!configurations.isEmpty(), "You must specify at least one configuration file");
        configs.addAll(configurations);

        start(args);
    }

    public static void start(@NotNull Class<?> mainConfigurationClass) {
        start(mainConfigurationClass, new String[0]);
    }

    public static void start(@NotNull Class<?> mainConfigurationClass, @NotNull String token) {
        checkNotNull(token, "Token cannot be null");

        start(mainConfigurationClass, of(format("--%s=%s", TELEGRAM_CLIENT_TOKEN, token)).toArray(new String[0]));
    }

    public static void start(@NotNull Class<?> mainConfigurationClass, @NotNull String[] args) {
        checkNotNull(mainConfigurationClass, "Configuration cannot be null");
        configs.add(mainConfigurationClass);

        start(args);
    }

    private static void start(String[] args) {
        log.debug("Starting bot platfrom with configurations [{}]", configs);
        Class[] configClassArray = new Class[configs.size()];
        run(copyOf(configs).toArray(configClassArray), args);
    }
}
