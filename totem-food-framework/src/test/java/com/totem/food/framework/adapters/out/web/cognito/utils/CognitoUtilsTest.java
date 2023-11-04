package com.totem.food.framework.adapters.out.web.cognito.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CognitoUtilsTest {

    @Test
    void testCalculateSecretHashWhenValidInputsThenReturnSecretHash() throws NoSuchAlgorithmException, InvalidKeyException {

        // Given - Mocks
        String userPoolClientId = "testClientId";
        String userPoolClientSecret = "testClientSecret";
        String userName = "testUserName";

        //## When
        String result = CognitoUtils.calculateSecretHash(userPoolClientId, userPoolClientSecret, userName);

        //## Then
        assertNotNull(result);
        assertTrue(result.contains("ssRd3Er5zlD2b6+"));
    }

}