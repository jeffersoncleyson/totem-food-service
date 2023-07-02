package com.totem.food.application.ports.in.utils;

import com.totem.food.application.exceptions.InvalidHashingParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static String hash256(String param){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(param.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidHashingParam("Invalid input String");
        }
    }
}
