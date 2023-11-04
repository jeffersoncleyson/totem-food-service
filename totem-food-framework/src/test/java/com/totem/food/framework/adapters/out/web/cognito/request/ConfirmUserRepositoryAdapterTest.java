package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.out.persistence.commons.IConfirmRepositoryPort;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import com.totem.food.framework.adapters.out.web.cognito.utils.CognitoUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CodeMismatchException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ExpiredCodeException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ConfirmUserRepositoryAdapterTest {

    private IConfirmRepositoryPort<CustomerConfirmDto, Boolean> iConfirmRepositoryPort;

    @Mock
    private Environment env;
    @Mock
    private CognitoClient cognitoClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(env.getProperty("cognito.userPool.clientId")).thenReturn("clientId");
        Mockito.when(env.getProperty("cognito.userPool.clientSecret")).thenReturn("clientSecret");
        iConfirmRepositoryPort = new ConfirmUserRepositoryAdapter(env, cognitoClient);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    @DisplayName("Confirm customer with correct cpf and code")
    void confirmItem() {

        //### Given
        final var confirmation = new CustomerConfirmDto("cpf", "code");

        ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                .clientId("clientId")
                .confirmationCode(confirmation.getCode())
                .username(confirmation.getCpf())
                .secretHash("base64SecretHash")
                .build();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.confirmSignUp(signUpRequest)).thenReturn(ConfirmSignUpResponse.builder().build());
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                    () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            assertDoesNotThrow(() -> iConfirmRepositoryPort.confirmItem(confirmation));
        }
    }

    private CustomerConfirmDto mockCognitoIntegrationErrors(Class aClass){

        //### Given
        final var confirmation = new CustomerConfirmDto("cpf", "code");

        ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                .clientId("clientId")
                .confirmationCode(confirmation.getCode())
                .username(confirmation.getCpf())
                .secretHash("base64SecretHash")
                .build();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.confirmSignUp(signUpRequest)).thenThrow(aClass);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        return confirmation;
    }

    @Test
    @DisplayName("Confirm customer with expired code")
    void confirmItemCodeExpired() {

        final var confirmation = mockCognitoIntegrationErrors(ExpiredCodeException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(InvalidInput.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("Code is expired or invalid", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Confirm customer with invalid code")
    void confirmItemInvalidCode() {

        final var confirmation = mockCognitoIntegrationErrors(CodeMismatchException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(InvalidInput.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("Code is expired or invalid", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Confirm customer with invalid user")
    void confirmItemInvalidUser() {

        final var confirmation = mockCognitoIntegrationErrors(NotAuthorizedException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(InvalidInput.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("User not authorized", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Confirm customer with non-existing user")
    void confirmItemNonExistingUser() {

        final var confirmation = mockCognitoIntegrationErrors(UserNotFoundException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(InvalidInput.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("User not authorized", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Confirm customer with integration error")
    void confirmItemIntegrationError() {

        final var confirmation = mockCognitoIntegrationErrors(CognitoIdentityProviderException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(ExternalCommunicationInvalid.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("Error to integrate with user service", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Confirm customer with invalid key")
    void confirmItemJavaSecurityErrorInvalidKey() {

        //### Given
        final var confirmation = new CustomerConfirmDto("cpf", "code");
        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenThrow(InvalidKeyException.class);
            final var expection = assertThrows(ExternalCommunicationInvalid.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("Error to integrate with user service", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Confirm customer with invalid algorithm")
    void confirmItemJavaSecurityErrorNoSuchAlgorithmException() {

        //### Given
        final var confirmation = new CustomerConfirmDto("cpf", "code");
        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenThrow(NoSuchAlgorithmException.class);
            final var expection = assertThrows(ExternalCommunicationInvalid.class, () -> iConfirmRepositoryPort.confirmItem(confirmation));

            assertEquals("Error to integrate with user service", expection.getMessage());
        }
    }
}