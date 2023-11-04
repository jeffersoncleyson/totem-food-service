package com.totem.food.framework.adapters.out.web.cognito.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.Objects;

@Component
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CognitoClient {

    private CognitoIdentityProviderClient client;

    @SuppressWarnings("java:S6242")
    public CognitoIdentityProviderClient connect(){
        if(Objects.nonNull(this.client)){
            return this.client;
        }
        return CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
