package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.Optional;

@Component
public class ExistsCustomerRepositoryAdapter implements IExistsRepositoryPort<CustomerModel, Boolean> {

    private final CognitoClient cognitoClient;
    private final String userPoolId;

    public ExistsCustomerRepositoryAdapter(Environment env, CognitoClient cognitoClient) {
        this.userPoolId = env.getProperty("cognito.userPool.id");
        this.cognitoClient = cognitoClient;
    }


    @Override
    public Boolean exists(CustomerModel item) {
        try (CognitoIdentityProviderClient client = cognitoClient.connect()) {

            String filter = "username = \"%s\"".formatted(item.getCpf());
            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .filter(filter)
                    .build();

            ListUsersResponse response = client.listUsers(usersRequest);
            Optional<UserType> user = response.users().stream().findFirst();

            if(user.isPresent()){
                return true;
            }


        } catch (CognitoIdentityProviderException e) {
            throw new ExternalCommunicationInvalid("Error to integrate with user service");
        }

        return false;
    }
}
