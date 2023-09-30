package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import com.totem.food.framework.adapters.out.web.cognito.config.CognitoClient;
import com.totem.food.framework.adapters.out.web.cognito.request.SearchCustomerRepositoryAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static mocks.adapters.out.persistence.mongo.customer.entity.CustomerEntityMock.getMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryAdapterTest {

    private SearchCustomerRepositoryAdapter customerRepositoryAdapter;

    @Mock
    private Environment env;

    @Mock
    private CognitoClient cognito;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        customerRepositoryAdapter = new SearchCustomerRepositoryAdapter(env, cognito);
    }

    @Test
    void findAll() {

        //## Given
        final var customerModel = getMock();
        final var customerFilter = new CustomerFilterDto("Name");

        final var uuid = UUID.randomUUID().toString();
        final var sub = AttributeType.builder()
                .name("sub")
                .value(uuid)
                .build();
        final var customName = AttributeType.builder()
                .name("custom:name")
                .value(customerModel.getName())
                .build();
        final var customCpf = AttributeType.builder()
                .name("custom:cpf")
                .value(customerModel.getCpf())
                .build();
        final var email = AttributeType.builder()
                .name("email")
                .value(customerModel.getEmail())
                .build();
        final var customMobile = AttributeType.builder()
                .name("custom:mobile")
                .value(customerModel.getMobile())
                .build();
        final var attributes = List.of(sub,
                customName,
                customCpf,
                email,
                customMobile);


        final var cognitoClient = Mockito.mock(CognitoIdentityProviderClient.class);
        final var user = UserType.builder()
                .userCreateDate(customerModel.getCreateAt().toInstant())
                .userLastModifiedDate(customerModel.getModifiedAt().toInstant())
                .attributes(attributes)
                .build();
        final var listUsersResponse = ListUsersResponse.builder().users(List.of(user)).build();

        ListUsersRequest usersRequest = ListUsersRequest.builder()
                .userPoolId(Mockito.any())
                .build();

        Mockito.when(cognitoClient.listUsers(usersRequest)).thenReturn(listUsersResponse);
        Mockito.when(cognito.connect()).thenReturn(cognitoClient);


        //## When
        var listCategoryDomain = customerRepositoryAdapter.findAll(customerFilter);

        //## Then
        assertTrue(CollectionUtils.isNotEmpty(listCategoryDomain));
        assertEquals(1, listCategoryDomain.size());
        assertThat(listCategoryDomain.get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(customerModel);
        assertEquals(uuid, listCategoryDomain.get(0).getId());
    }

}