package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeleteUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerRepositoryAdapterTest {

    private IRemoveRepositoryPort<CustomerModel> iRemoveRepositoryPort;

    @Mock
    private CognitoClient cognitoClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iRemoveRepositoryPort = new DeleteCustomerRepositoryAdapter(cognitoClient);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    @DisplayName("Delete customer with null Authorization Header")
    void removeItemNullAuthorization() {
        assertThrows(InvalidInput.class, () -> iRemoveRepositoryPort.removeItem(null));
    }

    @Test
    @DisplayName("Delete customer with missing Authorization Header")
    void removeItemMissingAuthorizationBearerToken() {
        assertThrows(InvalidInput.class, () -> iRemoveRepositoryPort.removeItem(""));
    }

    @Test
    @DisplayName("Delete customer with correct Authorization Header")
    void removeItemCorrectAuthorizationBearerToken() {

        //## Given
        final var tokenJwt = "jwt";
        DeleteUserRequest deleteRequest = DeleteUserRequest.builder()
                .accessToken(tokenJwt)
                .build();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.deleteUser(deleteRequest)).thenReturn(DeleteUserResponse.builder().build());
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When and Then
        assertDoesNotThrow(() -> iRemoveRepositoryPort.removeItem(
                OAuth2AccessTokenInterceptor.BEARER.concat(" ").concat(tokenJwt)
            )
        );
    }

    @Test
    @DisplayName("Delete customer with invalid Authorization Header")
    void removeItemInvalidAuthorizationBearerToken() {

        //## Given
        final var jwt = "invalid_jwt";
        final var authorization = OAuth2AccessTokenInterceptor.BEARER.concat(" ").concat(jwt);
        DeleteUserRequest deleteRequest = DeleteUserRequest.builder()
                .accessToken(jwt)
                .build();

        final var client = Mockito.mock(CognitoIdentityProviderClient.class);
        Mockito.when(client.deleteUser(deleteRequest)).thenThrow(InvalidParameterException.class);
        Mockito.when(cognitoClient.connect()).thenReturn(client);

        //## When and Then
        final var expection = assertThrows(ExternalCommunicationInvalid.class, () -> iRemoveRepositoryPort.removeItem(authorization));
        assertEquals("Error to integrate with user service", expection.getMessage());
    }
}