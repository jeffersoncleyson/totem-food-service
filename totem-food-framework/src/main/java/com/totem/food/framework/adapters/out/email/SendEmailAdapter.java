package com.totem.food.framework.adapters.out.email;

import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import com.totem.food.application.ports.out.email.ISendEmailPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmailAdapter implements ISendEmailPort<EmailNotificationDto, Boolean> {

    @Override
    public Boolean sendEmail(EmailNotificationDto item) {

        log.info("\nSending E-mail:\n\tto: {}\n\tsubject: {}\n\tmessage: {}\n", item.getEmail(), item.getSubject(), item.getMessage());

        return Boolean.TRUE;
    }
}
