package com.totem.food.framework.adapters.out.email;

import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import mocks.dtos.EmailNotificationDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SendEmailAdapterTest {

    @InjectMocks
    private SendEmailAdapter sendEmailAdapter;

    @Test
    void sendEmail() {

        //## Given
        var emailNotificationDto = EmailNotificationDtoMock.getMock(UUID.randomUUID().toString());

        //## When
        Boolean result = sendEmailAdapter.sendEmail(emailNotificationDto);

        //## Then
        Assertions.assertTrue(result);

    }
}