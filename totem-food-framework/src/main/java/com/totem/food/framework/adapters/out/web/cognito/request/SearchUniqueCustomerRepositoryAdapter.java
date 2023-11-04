package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchUniqueCustomerRepositoryAdapter implements ISearchUniqueRepositoryPort<Optional<CustomerModel>> {

    private final CognitoClient cognitoClient;
    private final String userPoolId;

    public SearchUniqueCustomerRepositoryAdapter(Environment env, CognitoClient cognitoClient) {
        this.userPoolId = env.getProperty("cognito.userPool.id");
        this.cognitoClient = cognitoClient;
    }

    @Override
    public Optional<CustomerModel> findById(String id) {

        try (CognitoIdentityProviderClient client = cognitoClient.connect()) {

            String filter = "username = \"%s\"".formatted(id);
            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .filter(filter)
                    .build();

            ListUsersResponse response = client.listUsers(usersRequest);
            Optional<UserType> user = response.users().stream().findFirst();

            if (user.isPresent()) {

                Map<String, String> mapAttributes = user.get()
                        .attributes()
                        .stream()
                        .collect(Collectors.toMap(AttributeType::name, AttributeType::value));

                return Optional.of(new CustomerModel(
                        mapAttributes.getOrDefault("sub", null),
                        mapAttributes.getOrDefault("custom:name", null),
                        mapAttributes.getOrDefault("custom:cpf", ""),
                        mapAttributes.getOrDefault("email", ""),
                        mapAttributes.getOrDefault("custom:mobile", null),
                        null,
                        ZonedDateTime.ofInstant(user.get().userLastModifiedDate(), ZoneOffset.UTC),
                        ZonedDateTime.ofInstant(user.get().userCreateDate(), ZoneOffset.UTC)
                ));
            }


        } catch (CognitoIdentityProviderException e) {
            throw new ExternalCommunicationInvalid("Error to integrate with user service");
        }

        return Optional.empty();
    }

}
