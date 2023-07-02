package com.totem.food.framework.adapters.out.email;

import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import com.totem.food.application.ports.out.email.ISendEmailPort;
import com.totem.food.framework.adapters.out.email.config.EmailConfigs;
import mocks.dtos.EmailNotificationDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendEmailAdapterTest {

    private ISendEmailPort<EmailNotificationDto, Boolean> sendEmailAdapter;

    @Mock
    private EmailConfigs emailConfigs;
    private AutoCloseable closeable;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sendEmailAdapter = new SendEmailAdapter(emailConfigs);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void sendEmail() {

        //## Given
        when(emailConfigs.getHost()).thenReturn("host");
        when(emailConfigs.getPort()).thenReturn(1025);
        when(emailConfigs.getAuth()).thenReturn(false);
        when(emailConfigs.getTls()).thenReturn(false);
        when(emailConfigs.getEmail()).thenReturn("contato@totem.food.service.com.br");

        var emailNotificationDto = EmailNotificationDtoMock.getMock(UUID.randomUUID().toString());

        //## When
        Boolean result = sendEmailAdapter.sendEmail(emailNotificationDto);

        //## Then
        Assertions.assertTrue(result);

    }

    @Test
    void sendEmailWithAuthEnabled() {

        //## Given
        when(emailConfigs.getHost()).thenReturn("host");
        when(emailConfigs.getPort()).thenReturn(1025);
        when(emailConfigs.getAuth()).thenReturn(true);
        when(emailConfigs.getTls()).thenReturn(false);
        when(emailConfigs.getUsername()).thenReturn("username");
        when(emailConfigs.getPassword()).thenReturn("password");
        when(emailConfigs.getEmail()).thenReturn("contato@totem.food.service.com.br");

        var emailNotificationDto = EmailNotificationDtoMock.getMock(UUID.randomUUID().toString());

        //## When
        Boolean result = sendEmailAdapter.sendEmail(emailNotificationDto);

        //## Then
        Assertions.assertTrue(result);

    }
}