package com.zerhmouti.redditclone.service;

import com.zerhmouti.redditclone.exception.RedditCloneException;
import com.zerhmouti.redditclone.model.NotificationEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.management.Notification;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Async
    public void send(NotificationEmail notificationEmail) throws RedditCloneException {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("yassinehesgone@gmail.com");
            helper.setTo(notificationEmail.getRecipient());
            helper.setSubject(notificationEmail.getSubject());
            helper.setText(notificationEmail.getBody()); ;
        };
        try{
            mailSender.send(mimeMessagePreparator);
            log.info("Activation email sent!!");
        }catch (MailException e){
            throw new RedditCloneException("Exception occured when sending mail to "+ notificationEmail.getRecipient());
        }

    }
}
