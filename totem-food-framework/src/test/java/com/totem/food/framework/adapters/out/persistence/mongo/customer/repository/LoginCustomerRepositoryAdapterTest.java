package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoginCustomerRepositoryAdapterTest {

    @Mock
    private LoginCustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;

    @Spy
    private ICustomerEntityMapper iCustomerEntityMapper = Mappers.getMapper(ICustomerEntityMapper.class);

    private LoginCustomerRepositoryAdapter loginCustomerRepositoryAdapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        loginCustomerRepositoryAdapter = new LoginCustomerRepositoryAdapter(repository, iCustomerEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void findByCadastre() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "John";
        final var email = "john@gmail.com";
        final var cpf = "51397955074";
        final var mobile = "5511900112233";
        final var password = "%#AjOBF%w.<K";
        final var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var customerDomain = new CustomerDomain();
        customerDomain.setId(id);
        customerDomain.setName(name);
        customerDomain.setCpf(cpf);
        customerDomain.setEmail(email);
        customerDomain.setMobile(mobile);
        customerDomain.setPassword(password);
        customerDomain.setCreateAt(createAt);
        customerDomain.setModifiedAt(modifiedAt);

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
        when(repository.findByCpfAndPassword(cpf, password)).thenReturn(customerEntity);

        //### When
        final var customerDomainSaved = loginCustomerRepositoryAdapter.findByCadastro(cpf, password);

        //### Then
        verify(repository, times(1)).findByCpfAndPassword(anyString(), anyString());
        verify(iCustomerEntityMapper, times(1)).toModel(any(CustomerEntity.class));

        assertThat(customerDomainSaved)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isNotNull();

        assertEquals(customerEntity.getCpf(), customerDomainSaved.get().getCpf());
    }
}