package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
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
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static mocks.adapters.out.persistence.mongo.customer.entity.CustomerEntityMock.getMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchUniqueCustomerRepositoryAdapterTest {

    private ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueRepositoryPort;

    @Mock
    private Environment env;
    @Mock
    private CognitoClient cognitoClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(env.getProperty("cognito.userPool.id")).thenReturn("id");
        iSearchUniqueRepositoryPort = new SearchUniqueCustomerRepositoryAdapter(env, cognitoClient);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    @DisplayName("Find unique customer")
    void findById() {

        //## Given
        final var customerModel = getMock();

        final var sub = AttributeType.builder()
                .name("sub")
                .value(customerModel.getId())
                .build();
        final var customName = AttributeType.builder()
                .name("custom:name")
                .value(customerModel.getName())
                .build();
        final var customCpf = AttributeType.builder()
                .name("custom:cpf")
                .value(customerModel.getCpf())
                .build();
        final var email = AttributeType.builder()
                .name("email")
                .value(customerModel.getEmail())
                .build();
        final var customMobile = AttributeType.builder()
                .name("custom:mobile")
                .value(customerModel.getMobile())
                .build();
        final var attributes = List.of(sub,
                customName,
                customCpf,
                email,
                customMobile);


        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        final var user = UserType.builder()
                .userCreateDate(customerModel.getCreateAt().toInstant())
                .userLastModifiedDate(customerModel.getModifiedAt().toInstant())
                .attributes(attributes)
                .build();
        final var listUsersResponse = ListUsersResponse.builder().users(List.of(user)).build();

        final var filter = "sub = \"%s\"".formatted(customerModel.getId());
        ListUsersRequest usersRequest = ListUsersRequest.builder()
                .userPoolId(Mockito.any())
                .filter(filter)
                .build();

        Mockito.when(client.listUsers(usersRequest)).thenReturn(listUsersResponse);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When
        final var customer = assertDoesNotThrow(() -> iSearchUniqueRepositoryPort.findById(customerModel.getId()));

        //## Then
        assertTrue(customer.isPresent());

        customer.ifPresent(c -> {
            assertThat(customerModel)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(c);
        });

    }

    @Test
    @DisplayName("Find unique customer but not found")
    void findByIdNotFound() {

        //## Given
        final var customerModel = getMock();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        final var listUsersResponse = ListUsersResponse.builder().users(List.of()).build();

        final var filter = "sub = \"%s\"".formatted(customerModel.getId());
        ListUsersRequest usersRequest = ListUsersRequest.builder()
                .userPoolId(Mockito.any())
                .filter(filter)
                .build();

        Mockito.when(client.listUsers(usersRequest)).thenReturn(listUsersResponse);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When
        final var customer = assertDoesNotThrow(() -> iSearchUniqueRepositoryPort.findById(customerModel.getId()));

        //## Then
        assertFalse(customer.isPresent());
    }

    @Test
    @DisplayName("Find unique but cognito client throws exception")
    void findByIdCognitoClientException() {

        //## Given
        final var id = UUID.randomUUID().toString();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.listUsers(Mockito.any(ListUsersRequest.class))).thenThrow(CognitoIdentityProviderException.class);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When
        final var exception = assertThrows(ExternalCommunicationInvalid.class, () -> iSearchUniqueRepositoryPort.findById(id));

        //## Then
        assertEquals("Error to integrate with user service", exception.getMessage());

    }
}