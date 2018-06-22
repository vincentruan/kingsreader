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
package com.github.springtg.bot.manager;

import com.github.springtg.bot.MailInlineResource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Component
public class EmailNotifyManager {

    @Autowired
    private JavaMailSender mailSender;


    /**
     *
     * @param simpleMailMessage 邮件内容
     * @param attachments 附件内容 key:附件名称，value附件
     * @param inlineResources 内联资源，如上面嵌入图片ID为pic_id，则这里需要加入contentId为pic_id的图片文件
     *                        <code>
     *                              <html><body><img src="cid:pic_id" ></body></html>
     *                        </code>
     * @throws Exception
     * @see SimpleMailMessage
     * @see MimeMessageHelper
     */
    public void sendMail(@Nonnull SimpleMailMessage simpleMailMessage, @Nullable Map<String, File> attachments, @Nullable List<MailInlineResource> inlineResources) throws Exception {
        Assert.notNull(simpleMailMessage, "Mail message object can not be null.");

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        Assert.hasText(simpleMailMessage.getFrom(), "from address must has text.");
        helper.setFrom(simpleMailMessage.getFrom());

        Assert.noNullElements(simpleMailMessage.getTo(), "Send address can not be empty!");
        helper.setTo(simpleMailMessage.getTo());

        Assert.hasText(simpleMailMessage.getSubject(), "subject must has text.");
        helper.setSubject(simpleMailMessage.getSubject());

        //需要注意的是addInline函数中资源名称xxx需要与正文中cid:xxx对应起来
        String text = simpleMailMessage.getText();
        if(CollectionUtils.isNotEmpty(inlineResources)) {
            helper.setText(text, true);

            for (MailInlineResource inlineResource : inlineResources) {
                if(null != inlineResource) {
                    helper.addInline(inlineResource.getContentId(), inlineResource.getFile());
                }
            }

        } else {
            helper.setText(text, false);
        }

        if(MapUtils.isNotEmpty(attachments)) {
            for (Map.Entry<String, File> stringFileEntry : attachments.entrySet()) {
                helper.addAttachment(stringFileEntry.getKey(), stringFileEntry.getValue());
            }
        }

        mailSender.send(mimeMessage);
    }

}
