package com.github.springtg.bot.platform.client.command;

/**
 * @author Sergey Kuptsov
 * @since 01/06/2016
 */
public interface ApiCommandSender {
    void sendCommand(ApiCommand apiCommand);
}
