package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.utils.CognitoUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class ExistsCustomerRepositoryAdapter implements IExistsRepositoryPort<CustomerModel, Boolean> {

    private final String userPoolId;

    public ExistsCustomerRepositoryAdapter(Environment env) {
        this.userPoolId = env.getProperty("cognito.userPool.id");
    }


    @Override
    public Boolean exists(CustomerModel item) {
        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("soat1"))
                .build()) {

            String filter = "username = \"%s\"".formatted(item.getCpf());
            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .filter(filter)
                    .build();

            ListUsersResponse response = cognitoClient.listUsers(usersRequest);
            Optional<UserType> user = response.users().stream().findFirst();

            if(user.isPresent()){
                return true;
            }


        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
