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
package com.github.springtg.bot.kingsreader.event;

import lombok.extern.slf4j.Slf4j;
import man.autolife.autobooking.service.notify.AutoBookingMailNotifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Component
@Slf4j
public class MailNotifyEventListener {

    @Autowired
    private AutoBookingMailNotifyService autoBookingMailNotifyService;

    @Value("${app.autobooking.mail.to}")
    private String[] mailTo;

    @Async
    @EventListener
    void asyncNotifyAutoBooking(AutoBookingSuccessEvent event) throws Exception {
        log.info("received auto booking success event: {}", event);

        String subject = "自动预定" + event.getBookingDate() + "的模拟课程成功";
        String mailBody = "自动预定" + event.getBookingDate() + "的模拟课程成功, 预定时段: " + StringUtils.join(event.getBookingTimeFrames(), ',');
        autoBookingMailNotifyService.sendSimpleMail(mailTo, subject, mailBody);
    }

}
