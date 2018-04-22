package com.github.storage.dropbox.config;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for dropbox settings.
 * @author vincentruan
 */
@Configuration
@ConditionalOnClass({DbxRequestConfig.class})
@EnableConfigurationProperties(DropboxConfigProperties.class)
public class DropboxConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    private final DropboxConfigProperties dropboxConfigProperties;

    public DropboxConfiguration(final DropboxConfigProperties dropboxConfigProperties) {
        this.dropboxConfigProperties = dropboxConfigProperties;
    }

    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return new DbxRequestConfig(appName);
    }

    @Bean
    public DbxWebAuth dbxWebAuth(final DbxRequestConfig requestConfig) {
        final DbxAppInfo appInfo = new DbxAppInfo(dropboxConfigProperties.getKey(), dropboxConfigProperties.getSecret());
        return new DbxWebAuth(requestConfig, appInfo);
    }
}
