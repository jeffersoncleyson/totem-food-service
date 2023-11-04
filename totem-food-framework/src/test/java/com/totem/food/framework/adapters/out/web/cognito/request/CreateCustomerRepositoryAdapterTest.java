package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import com.totem.food.framework.adapters.out.web.cognito.utils.CognitoUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CodeDeliveryFailureException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InvalidPasswordException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

import java.util.ArrayList;
import java.util.List;

import static mocks.adapters.out.persistence.mongo.customer.entity.CustomerEntityMock.getMock;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateCustomerRepositoryAdapterTest {
    private ICreateRepositoryPort<CustomerModel> iCreateRepositoryPort;

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
        iCreateRepositoryPort = new CreateCustomerRepositoryAdapter(env, cognitoClient);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    @DisplayName("Save customer")
    void saveItem() {

        //## Given
        final var customerModel = getMock();

        AttributeType email = AttributeType.builder()
                .name("email")
                .value(customerModel.getEmail())
                .build();

        AttributeType cpf = AttributeType.builder()
                .name("custom:cpf")
                .value(customerModel.getCpf())
                .build();

        List<AttributeType> userAttrsList = new ArrayList<>();
        userAttrsList.add(email);
        userAttrsList.add(cpf);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userAttributes(userAttrsList)
                .username(customerModel.getCpf())
                .clientId("clientId")
                .secretHash("base64SecretHash")
                .password(customerModel.getPassword())
                .build();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.signUp(signUpRequest)).thenReturn(SignUpResponse.builder().build());
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            assertDoesNotThrow(() -> iCreateRepositoryPort.saveItem(customerModel));
        }
    }

    private void mockCognitoIntegrationErrors(Class aClass){

        //### Given
        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.signUp(Mockito.any(SignUpRequest.class))).thenThrow(aClass);
        Mockito.when(cognitoClient.connect()).thenReturn(client);
    }

    @Test
    @DisplayName("Save customer but user exits")
    void saveItemUserExists() {

        //### Given
        final var customerModel = getMock();
        mockCognitoIntegrationErrors(UsernameExistsException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(InvalidInput.class, () -> iCreateRepositoryPort.saveItem(customerModel));

            assertEquals("Error username already exists", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Save customer with invalid password")
    void saveItemInvalidPassword() {

        //### Given
        final var customerModel = getMock();
        mockCognitoIntegrationErrors(InvalidPasswordException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(InvalidInput.class, () -> iCreateRepositoryPort.saveItem(customerModel));

            assertEquals("Error invalid password", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Save customer but code delivery failed")
    void saveItemCodeDeliveryError() {

        //### Given
        final var customerModel = getMock();
        mockCognitoIntegrationErrors(CodeDeliveryFailureException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(ExternalCommunicationInvalid.class, () -> iCreateRepositoryPort.saveItem(customerModel));

            assertEquals("Error to delivery code", expection.getMessage());
        }
    }

    @Test
    @DisplayName("Save customer but cognito client throws general exception")
    void saveItemCognitoClientGeneralException() {

        //### Given
        final var customerModel = getMock();
        mockCognitoIntegrationErrors(CognitoIdentityProviderException.class);

        //### When
        try (MockedStatic<CognitoUtils> cognitoUtilsMockedStatic = Mockito.mockStatic(CognitoUtils.class)) {
            cognitoUtilsMockedStatic.when(
                            () -> CognitoUtils.calculateSecretHash("clientId", "clientSecret", "cpf"))
                    .thenReturn("base64SecretHash");
            final var expection = assertThrows(ExternalCommunicationInvalid.class, () -> iCreateRepositoryPort.saveItem(customerModel));

            assertEquals("Error to integrate with user service", expection.getMessage());
        }
    }
}