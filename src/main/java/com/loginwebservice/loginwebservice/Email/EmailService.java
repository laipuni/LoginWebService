package com.loginwebservice.loginwebservice.Email;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderMail;

    private final SpringTemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    public boolean sendEmail(final String toEmail,final String context,final String title){
        mailSender.send(createMailMessage(toEmail,context,title));
        return true;
    }

    public boolean sendEmail(final String toEmail, final String templatePath, final Map<String,String> variables, final String title){
        mailSender.send(
                createMailMessage(
                        toEmail,
                        resolveHtml(templatePath,variables),
                        title
                )
        );
        return true;
    }

    private MimeMessage createMailMessage(final String toEmail, final String context,final String title){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, toEmail);
            message.setSubject(title);
            message.setFrom(senderMail);
            message.setText(context,"utf-8","html");
        } catch (MessagingException exception){
            log.debug("mail error = {}",exception.getCause());
            throw new IllegalArgumentException("알 수 없는 에러가 발생했습니다.",exception.getCause());
        }
        return message;
    }

    private String resolveHtml(final String templatePath,final Map<String,String> variables){
        Context context = new Context();
        variables.keySet()
                .forEach((value) ->
                        context.setVariable(value,variables.get(value))
                );
        return templateEngine.process(templatePath, context);
    }
}
