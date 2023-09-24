package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.out.persistence.commons.IConfirmRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.utils.CognitoUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class ConfirmUserRepositoryAdapter implements IConfirmRepositoryPort<CustomerConfirmDto, Boolean> {

    private final String userPoolClientId;
    private final String userPoolClientSecret;

    public ConfirmUserRepositoryAdapter(Environment env) {
        this.userPoolClientId = env.getProperty("cognito.userPool.clientId");
        this.userPoolClientSecret = env.getProperty("cognito.userPool.clientSecret");
    }

    @Override
    public Boolean confirmItem(CustomerConfirmDto item) {

        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("soat1"))
                .build()) {
            String secretHash = CognitoUtils.calculateSecretHash(
                    userPoolClientId,
                    userPoolClientSecret,
                    item.getCustomerId()
            );

            ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(userPoolClientId)
                    .confirmationCode(item.getCode())
                    .username(item.getCustomerId())
                    .secretHash(secretHash)
                    .build();

            cognitoClient.confirmSignUp(signUpRequest);

        } catch (CognitoIdentityProviderException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
