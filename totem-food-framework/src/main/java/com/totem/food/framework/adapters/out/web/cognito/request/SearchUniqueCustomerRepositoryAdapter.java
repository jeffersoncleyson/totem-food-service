package com.totem.food.framework.adapters.out.web.cognito.request;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchUniqueCustomerRepositoryAdapter implements ISearchUniqueRepositoryPort<Optional<CustomerModel>> {

    private final ICustomerEntityMapper iCustomerEntityMapper;
    private final String userPoolId;

    public SearchUniqueCustomerRepositoryAdapter(Environment env, ICustomerEntityMapper iCustomerEntityMapper) {
        this.userPoolId = env.getProperty("cognito.userPool.id");
        this.iCustomerEntityMapper = iCustomerEntityMapper;
    }

    @Override
    public Optional<CustomerModel> findById(String id) {

        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create("soat1"))
                .build()) {

            String filter = "sub = \"%s\"".formatted(id);
            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .filter(filter)
                    .build();

            ListUsersResponse response = cognitoClient.listUsers(usersRequest);
            Optional<UserType> user = response.users().stream().findFirst();

            if(user.isPresent()){

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
                    "",
                    null,
                    null
                ));
            }


        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

}
