package com.totem.food.framework.adapters.out.web.cognito.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CognitoClientTest {

    @InjectMocks
    private CognitoClient cognitoClient;

    @Test
    void testConnectWhenClientNullThenCreateNewClient() {

        //## Given
        ReflectionTestUtils.setField(cognitoClient, "client", null);

        //## When
        CognitoIdentityProviderClient result = cognitoClient.connect();

        //## Then
        assertThat(result).usingRecursiveAssertion().isNotNull();
        assertEquals(Region.US_EAST_1, result.serviceClientConfiguration().region());

    }

    @Test
    void testConnectWhenClientNonNull() {

        // Given - When
        CognitoIdentityProviderClient client = assertDoesNotThrow(() -> cognitoClient.connect(),
                "The connect method should not throw any exceptions");

        //## Then
        assertNotNull(client);
    }
}