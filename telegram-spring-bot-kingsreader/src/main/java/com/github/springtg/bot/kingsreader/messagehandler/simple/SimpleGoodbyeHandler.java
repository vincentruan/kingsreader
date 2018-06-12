/*
 * Copyright [2018] [vincentruan]
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
package com.github.springtg.bot.kingsreader.messagehandler.simple;

import com.github.springtg.bot.platform.client.command.Reply;
import com.github.springtg.bot.platform.client.command.ReplyTo;
import com.github.springtg.bot.platform.handler.MessageTextMessageHandler;
import com.github.springtg.bot.platform.model.UpdateEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.of;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Component
public class SimpleGoodbyeHandler implements MessageTextMessageHandler {

    @Override
    public Set<String> getMessageText() {
        return of("Bye", "GoodBye");
    }

    @Override
    public Reply handle(UpdateEvent updateEvent) {
        return ReplyTo.to(updateEvent).withMessage("Goodbye! See you soon!");
    }
}