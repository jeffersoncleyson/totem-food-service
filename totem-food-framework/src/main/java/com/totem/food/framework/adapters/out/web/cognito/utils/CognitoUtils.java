package com.totem.food.framework.adapters.out.web.cognito.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CognitoUtils {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) throws NoSuchAlgorithmException, InvalidKeyException {

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);

        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        mac.update(userName.getBytes(StandardCharsets.UTF_8));
        byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(rawHmac);
    }
}
