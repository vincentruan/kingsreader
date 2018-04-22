package com.github.springtg.bot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * Parses users' ids information from the Dropbox Webhook notification message.
 * It only contains users, who make changes.
 * @author vincentruan
 */
@Service
@Slf4j
public class DeltaUsersParserService {

    @Autowired
    private ObjectMapper objectMapper;

    public List<String> getUsers(final String notification) {
        List<String> users = null;
        try {
            users = getJsonUsersFromNotificationBody(notification);
            log.debug("Parse users successfully from notification: '{}'", notification);
        } catch (final Exception e) {
            log.error("An error occured while try to parse users: ", e);
            users = emptyList();
        }
        return users;
    }

    @SuppressWarnings("unchecked")
    private List<String> getJsonUsersFromNotificationBody(final String notification) throws IOException {
        Map<String, Object> jsonData = objectMapper.readValue(notification, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> delta = (Map<String, Object>) MapUtils.getMap(jsonData, "delta");
        return (List<String>) MapUtils.getObject(delta,"users");
    }
}
