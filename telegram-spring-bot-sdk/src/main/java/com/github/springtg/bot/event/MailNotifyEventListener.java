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
package com.github.springtg.bot.event;

import com.github.springtg.bot.RichMailMessageEvent;
import com.github.springtg.bot.manager.EmailNotifyManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Component
@Slf4j
public class MailNotifyEventListener {

    @Autowired
    private EmailNotifyManager emailNotifyManager;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    @EventListener
    void asyncNotifyAutoBooking(RichMailMessageEvent event) throws Exception {
        log.info("received auto booking success event: {}", event);
        if(null == event) {
            return;
        }

        if(StringUtils.isBlank(event.getText())) {
            if (StringUtils.isNotBlank(event.getTemplateName())) {
                log.warn("mail body is empty, ignore!");
                return;
            }
            String emailContent = templateEngine.process(event.getTemplateName(), event.getContext());
            event.setText(emailContent);
        }

        emailNotifyManager.sendMail(event, event.getAttachments(), event.getInlineResources());
    }

}
