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
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Component
public class EmailNotifyManager {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.autobooking.mail.from}")
    private String from;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 
     * @param mailTo 收件人列表
     * @param subject 邮件标题
     * @param mailBody 邮件正文
     */
    public void sendSimpleMail(String[] mailTo, String subject, String mailBody) {
        Assert.noNullElements(mailTo, "Send address can not be empty!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailTo);
        message.setSubject(subject);
        message.setText(mailBody);
        mailSender.send(message);
    }

    /**
     * 
     * @param mailTo 收件人列表
     * @param subject 邮件标题
     * @param mailBody 邮件正文
     * @param attachments 邮件附件
     * @throws Exception
     */
    public void sendMail(String[] mailTo, String subject, String mailBody, File... attachments) throws Exception {
        Assert.noNullElements(mailTo, "Send address can not be empty!");

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(mailTo);
        helper.setSubject(subject);
        helper.setText(mailBody);

        if(ArrayUtils.isNotEmpty(attachments)) {
            for (File attachment : attachments) {
                if(null != attachment) {
                    helper.addAttachment(attachment.getName(), attachment);
                }
            }
        }

        mailSender.send(mimeMessage);
    }

    /**
     *
     * @param mailTo 收件人列表
     * @param subject 邮件标题
     * @param mailBody HTML完整代码, 如下面代码表示嵌入一段图片，图片ID为pic_id
     *                 <code>
     *                     <html><body><img src="cid:pic_id" ></body></html>
     *                 </code>
     * @param inlineResources 内联资源，如上面嵌入图片ID为pic_id，则这里需要加入contentId为pic_id的图片文件
     * @throws Exception
     */
    public void sendInlineMail(String[] mailTo, String subject, String mailBody, MailInlineResource... inlineResources) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(mailTo);
        helper.setSubject(subject);
        helper.setText(mailBody, true);

        //需要注意的是addInline函数中资源名称xxx需要与正文中cid:xxx对应起来
        if(ArrayUtils.isNotEmpty(inlineResources)) {
            for (MailInlineResource inlineResource : inlineResources) {
                if(null != inlineResource) {
                    helper.addInline(inlineResource.getContentId(), inlineResource.getFile());
                }
            }
        }

        mailSender.send(mimeMessage);
    }

    /**
     * 
     * @param mailTo 收件人列表
     * @param subject 邮件标题
     * @param templateName
     * @param context
     * @throws Exception
     */
    public void sendTemplateMail(final String[] mailTo, final String subject, final String templateName, final IContext context) throws Exception {
        String emailContent = templateEngine.process(templateName, context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(mailTo);
        mimeMessageHelper.setText(emailContent, true);
        mimeMessageHelper.setSubject(subject);
        mailSender.send(mimeMessage);
    }
}
