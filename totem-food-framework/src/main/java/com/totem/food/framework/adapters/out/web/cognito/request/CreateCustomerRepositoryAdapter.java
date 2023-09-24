package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.utils.CognitoUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateCustomerRepositoryAdapter implements ICreateRepositoryPort<CustomerModel> {

    private final String userPoolClientId;
    private final String userPoolClientSecret;

    public CreateCustomerRepositoryAdapter(Environment env) {
        this.userPoolClientId = env.getProperty("cognito.userPool.clientId");
        this.userPoolClientSecret = env.getProperty("cognito.userPool.clientSecret");
    }

    @Override
    public CustomerModel saveItem(CustomerModel item) {

        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("soat1"))
                .build()) {
            String secretHash = CognitoUtils.calculateSecretHash(
                    userPoolClientId,
                    userPoolClientSecret,
                    item.getCpf()
            );

            AttributeType email = AttributeType.builder()
                    .name("email")
                    .value(item.getEmail())
                    .build();

            AttributeType cpf = AttributeType.builder()
                    .name("custom:cpf")
                    .value(item.getCpf())
                    .build();

            List<AttributeType> userAttrsList = new ArrayList<>();
            userAttrsList.add(email);
            userAttrsList.add(cpf);

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(userAttrsList)
                    .username(item.getCpf())
                    .clientId(userPoolClientId)
                    .secretHash(secretHash)
                    .password(item.getPassword())
                    .build();

            cognitoClient.signUp(signUpRequest);

        } catch(CognitoIdentityProviderException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return item;
    }
}
