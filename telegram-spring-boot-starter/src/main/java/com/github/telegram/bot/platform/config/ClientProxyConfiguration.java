package com.github.telegram.bot.platform.config;

import org.asynchttpclient.Realm;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.proxy.ProxyType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 29/01/2017
 */
@Configuration
@ConfigurationProperties(prefix = "telegram.client.proxy")
public class ClientProxyConfiguration {

    private ProxyType proxyType = ProxyType.HTTP;
    private String host;
    private Integer port;
    private String principal;
    private String password;
    private List<String> nonProxyHosts;

    private ProxyServer proxyServer;

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    public void setProxyType(String protocol) {
        if (StringUtils.hasText(protocol)) {
            ProxyType[] allProxyTypes = ProxyType.values();
            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;
            for (ProxyType proxyType : allProxyTypes) {
                if (isFirst) {
                    sb.append(proxyType.name());
                } else {
                    sb.append(',').append(proxyType.name());
                }
                if (proxyType.name().equalsIgnoreCase(protocol)) {
                    this.proxyType = proxyType;
                    break;
                }
                isFirst = false;
            }
            throw new IllegalArgumentException("proxy type must in ProxyType list [" + sb.toString() + "], case insensitive!");
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public void setNonProxyHosts(String nonProxyHosts) {
        if (StringUtils.hasText(nonProxyHosts)) {
            String[] nonProxyHostArray = nonProxyHosts.split(",");
            this.nonProxyHosts = Arrays.asList(nonProxyHostArray);
        }
    }

    @PostConstruct
    public void init() {
        if (host != null && port != null) {
            proxyServer = new ProxyServer.Builder(host, port)
                    .setProxyType(proxyType)
                    .setNonProxyHosts(nonProxyHosts)
                    .setRealm(new Realm.Builder(principal, password))
                    .build();
        }
    }
}
