package com.totem.food.framework.adapters.out.email;

import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import com.totem.food.application.ports.out.email.ISendEmailPort;
import com.totem.food.framework.adapters.out.email.config.EmailConfigs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@AllArgsConstructor
@Component
@Slf4j
public class SendEmailAdapter implements ISendEmailPort<EmailNotificationDto, Boolean> {

    private final EmailConfigs emailConfigs;

    @Override
    public Boolean sendEmail(EmailNotificationDto item) {
        sendMail(item);
        return Boolean.TRUE;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", emailConfigs.getHost());
        properties.put("mail.smtp.port", emailConfigs.getPort());
        properties.put("mail.smtp.auth", emailConfigs.getAuth());
        properties.put("mail.smtp.starttls.enable", emailConfigs.getTls());

        if(emailConfigs.getAuth().equals(Boolean.TRUE)) {
            properties.put("mail.user", emailConfigs.getUsername());
            properties.put("mail.password", emailConfigs.getPassword());
        }

        return properties;
    }

    private void sendMail(EmailNotificationDto item) {

        try {
            Session session = Session.getInstance(getProperties());

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfigs.getEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(item.getEmail()));
            message.setSubject(item.getSubject());

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(item.getMessage(), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            log.error("Error: {}", e.getMessage());
            log.error("\nError Sending E-mail:\n\tto: {}\n\tsubject: {}\n\tmessage: {}\n", item.getEmail(), item.getSubject(), item.getMessage());
        }
    }
}
