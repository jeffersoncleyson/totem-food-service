package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchCustomerRepositoryAdapter implements ISearchRepositoryPort<CustomerFilterDto, List<CustomerModel>> {

    private final CognitoClient cognitoClient;
    private final String userPoolId;

    public SearchCustomerRepositoryAdapter(Environment env, CognitoClient cognitoClient) {
        this.userPoolId = env.getProperty("cognito.userPool.id");
        this.cognitoClient = cognitoClient;
    }

    @Override
    public List<CustomerModel> findAll(CustomerFilterDto filterCategoryDto) {

        final var customerModels = new ArrayList<CustomerModel>();

        try (CognitoIdentityProviderClient client = cognitoClient.connect()) {

            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .build();

            ListUsersResponse response = client.listUsers(usersRequest);
            response.users().forEach(u -> {
                Map<String, String> mapAttributes = u.attributes().stream().collect(Collectors.toMap(AttributeType::name, AttributeType::value));
                customerModels.add(new CustomerModel(
                        mapAttributes.getOrDefault("sub", null),
                        mapAttributes.getOrDefault("custom:name", null),
                        mapAttributes.getOrDefault("custom:cpf", ""),
                        mapAttributes.getOrDefault("email", ""),
                        mapAttributes.getOrDefault("custom:mobile", null),
                        null,
                        ZonedDateTime.ofInstant(u.userLastModifiedDate(), ZoneOffset.UTC),
                        ZonedDateTime.ofInstant(u.userCreateDate(), ZoneOffset.UTC)
                ));
            });

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException(e);
        }

        return customerModels;
    }
}
