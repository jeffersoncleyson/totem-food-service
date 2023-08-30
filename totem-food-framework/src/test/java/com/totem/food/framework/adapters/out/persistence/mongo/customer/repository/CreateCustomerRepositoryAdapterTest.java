package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerRepositoryAdapterTest {

    @Mock
    private CreateCustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;

    @Spy
    private ICustomerEntityMapper iCustomerEntityMapper = Mappers.getMapper(ICustomerEntityMapper.class);

    private CreateCustomerRepositoryAdapter createCustomerRepositoryAdapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createCustomerRepositoryAdapter = new CreateCustomerRepositoryAdapter(repository, iCustomerEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void saveItem() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "John";
        final var email = "john@gmail.com";
        final var cpf = "51397955074";
        final var mobile = "5511900112233";
        final var password = "%#AjOBF%w.<K";
        final var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var customerModel = new CustomerModel();
        customerModel.setId(id);
        customerModel.setName(name);
        customerModel.setCpf(cpf);
        customerModel.setEmail(email);
        customerModel.setMobile(mobile);
        customerModel.setPassword(password);
        customerModel.setCreateAt(createAt);
        customerModel.setModifiedAt(modifiedAt);

        final var customerEntity = new CustomerEntity();
        customerEntity.setId(id);
        customerEntity.setName(name);
        customerEntity.setCpf(cpf);
        customerEntity.setEmail(email);
        customerEntity.setMobile(mobile);
        customerEntity.setPassword(password);
        customerEntity.setCreateAt(createAt);
        customerEntity.setModifiedAt(modifiedAt);

        //### Given - Mocks
        when(repository.save(Mockito.any(CustomerEntity.class))).thenReturn(customerEntity);

        //### When
        final var customerDomainSaved = createCustomerRepositoryAdapter.saveItem(customerModel);

        //### Then
        verify(iCustomerEntityMapper, times(1)).toEntity(Mockito.any(CustomerModel.class));
        verify(repository, times(1)).save(Mockito.any(CustomerEntity.class));
        verify(iCustomerEntityMapper, times(1)).toModel(Mockito.any(CustomerEntity.class));

        assertThat(customerDomainSaved)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isNotNull();

        assertEquals(customerEntity.getCpf(), customerDomainSaved.getCpf());

    }
}