package com.github.springtg.bot.controller;

import com.github.springtg.bot.service.DeltaUsersParserService;
import com.github.springtg.bot.service.DropboxClientService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox. Once you register a URI to receive webhooks, Dropbox will
 * send an HTTP request to that URI every time there's a change for any of your app's registered users.
 */
@RestController
@RequestMapping("webhook")
@Slf4j
public class DropboxWebhookController {

    private final DeltaUsersParserService deltaUsersParserService;

    private final DropboxClientService dropboxClientService;

    public DropboxWebhookController(final DeltaUsersParserService deltaUsersParserService, final DropboxClientService dropboxClientService) {
        this.deltaUsersParserService = deltaUsersParserService;
        this.dropboxClientService = dropboxClientService;
    }

    /**
     * Once you enter your webhook URI, an initial "verification request" will be made to that URI. This verification is an HTTP GET request with a query
     * parameter called challenge.
     */
    @GetMapping
    public String getWebhookVerification(@RequestParam("challenge") final String challenge) {
        log.info("Respond to the webhook verification (GET request) by echoing back the challenge parameter.");
        return challenge;
    }

    /**
     * Once your webhook URI is added, your app will start receiving "notification requests" every time a user's files change. A notification request is an HTTP
     * POST request with a JSON body.
     */
    @PostMapping
    public void getFileData(@RequestBody final String notificationBody) throws Exception {
        log.info("Receive a list of changed user IDs from Dropbox and process each: '{}'", notificationBody);
        final List<String> userIds = deltaUsersParserService.getUsers(notificationBody);
        for (final String userId : userIds) {
            log.info("changed user id: '{}'", userId);
            dropboxClientService.logChangedFiles(userId);
        }
    }
}
