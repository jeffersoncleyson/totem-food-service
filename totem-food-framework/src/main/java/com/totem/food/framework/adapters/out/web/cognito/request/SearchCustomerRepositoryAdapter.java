package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchCustomerRepositoryAdapter implements ISearchRepositoryPort<CustomerFilterDto, List<CustomerModel>> {

    private final ICustomerEntityMapper iCustomerEntityMapper;
    private final String userPoolId;

    public SearchCustomerRepositoryAdapter(Environment env, ICustomerEntityMapper iCustomerEntityMapper) {
        this.userPoolId = env.getProperty("cognito.userPool.id");
        this.iCustomerEntityMapper = iCustomerEntityMapper;
    }

    @Override
    public List<CustomerModel> findAll(CustomerFilterDto filterCategoryDto) {

        final var customerModels = new ArrayList<CustomerModel>();

        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("soat1"))
                .build()) {

            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .build();

            ListUsersResponse response = cognitoClient.listUsers(usersRequest);
            response.users().forEach(u -> {
                Map<String, String> mapAttributes = u.attributes().stream().collect(Collectors.toMap(AttributeType::name, AttributeType::value));
                customerModels.add(new CustomerModel(
                        mapAttributes.getOrDefault("sub", null),
                        mapAttributes.getOrDefault("custom:name", null),
                        mapAttributes.getOrDefault("custom:cpf", ""),
                        mapAttributes.getOrDefault("email", ""),
                        mapAttributes.getOrDefault("custom:mobile", null),
                        "",
                        null,
                        null
                ));
            });

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException(e);
        }

        return customerModels;
    }
}
