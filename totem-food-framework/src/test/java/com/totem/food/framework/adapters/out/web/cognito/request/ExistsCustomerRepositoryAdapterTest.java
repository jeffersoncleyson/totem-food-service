package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;

import static mocks.adapters.out.persistence.mongo.customer.entity.CustomerEntityMock.getMock;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExistsCustomerRepositoryAdapterTest {

    private IExistsRepositoryPort<CustomerModel, Boolean> iExistsRepositoryPort;

    @Mock
    private Environment env;
    @Mock
    private CognitoClient cognitoClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(env.getProperty("cognito.userPool.id")).thenReturn("id");
        iExistsRepositoryPort = new ExistsCustomerRepositoryAdapter(env, cognitoClient);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    private Boolean verifyUserExitsTest(ListUsersResponse listUsersResponse){
        //## Given
        final var customerModel = getMock();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);

        final var filter = "username = \"%s\"".formatted(customerModel.getCpf());
        ListUsersRequest usersRequest = ListUsersRequest.builder()
                .userPoolId(Mockito.any())
                .filter(filter)
                .build();

        Mockito.when(client.listUsers(usersRequest)).thenReturn(listUsersResponse);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When
        return assertDoesNotThrow(() -> iExistsRepositoryPort.exists(customerModel));
    }

    @Test
    @DisplayName("Verify customer exists by cpf")
    void exists() {

        //## Given
        final var user = UserType.builder()
                .build();
        final var listUsersResponse = ListUsersResponse.builder().users(List.of(user)).build();

        //## Then
        final var result = verifyUserExitsTest(listUsersResponse);
        assertTrue(result);
    }

    @Test
    @DisplayName("Verify customer exists by cpf but not found")
    void existsNotFound() {

        //## Given
        final var listUsersResponse = ListUsersResponse.builder().users(List.of()).build();

        //## Then
        final var result = verifyUserExitsTest(listUsersResponse);
        assertFalse(result);
    }

    @Test
    @DisplayName("Verify customer exists by cpf but cognito client throws exception")
    void existsCognitoClientException() {

        //## Given
        final var customerModel = getMock();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.listUsers(Mockito.any(ListUsersRequest.class))).thenThrow(CognitoIdentityProviderException.class);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When
        final var exception = assertThrows(ExternalCommunicationInvalid.class, () -> iExistsRepositoryPort.exists(customerModel));

        //## Then
        assertEquals("Error to integrate with user service", exception.getMessage());

    }
}