/*
 * Copyright [2018] [70611]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.telegram.bot.handler;

import com.github.telegram.bot.platform.client.TelegramBotApi;
import com.github.telegram.bot.platform.client.command.Reply;
import com.github.telegram.bot.platform.config.BotPlatformStarter;
import com.github.telegram.bot.platform.handler.annotation.MessageHandler;
import com.github.telegram.bot.platform.handler.annotation.MessageMapping;
import com.github.telegram.bot.platform.model.UpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import static com.github.telegram.bot.platform.client.command.ReplyTo.to;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@MessageHandler
public class GreetingHandler {

    @Autowired
    private TelegramBotApi api;

    @MessageMapping(text = "/me")
    public Reply getMe(UpdateEvent updateEvent) {
        User me = api.getMe().get();

        Message reply = api.reply(to(updateEvent).withMessage(getMessageText(me))).get();

        return to(updateEvent).withMessage("Yes, that is me above!!!");
    }

    private String getMessageText(User me) {
        return "Me is : " + me.getFirstName() + " " + me.getLastName();
    }
}
