package com.totem.food.application.usecases.customer;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ILoginRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoginCustomerUserCaseTest {

    @Spy
    private ICustomerMapper iCustomerMapper = Mappers.getMapper(ICustomerMapper.class);

    @Mock
    ILoginRepositoryPort<Optional<CustomerModel>> iSearchUniqueRepositoryPort;

    private LoginCustomerUserCase loginCustomerUserCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        loginCustomerUserCase = new LoginCustomerUserCase(iCustomerMapper, iSearchUniqueRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }


    @Test
    void login() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "John";
        final var email = "john@gmail.com";
        final var cpf = "51397955074";
        final var mobile = "5511900112233";
        final var password = "%#AjOBF%w.<K";
        final var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var customerDto = new CustomerDto();
        customerDto.setName(name);
        customerDto.setEmail(email);
        customerDto.setCpf(cpf);
        customerDto.setMobile(mobile);
        customerDto.setModifiedAt(modifiedAt);
        customerDto.setCreateAt(createAt);

        final var customerModel = new CustomerModel();
        customerModel.setId(id);
        customerModel.setName(name);
        customerModel.setCpf(cpf);
        customerModel.setEmail(email);
        customerModel.setMobile(mobile);
        customerModel.setPassword(password);
        customerModel.setCreateAt(createAt);
        customerModel.setModifiedAt(modifiedAt);

        //### Given - Mocks
        when(iSearchUniqueRepositoryPort.findByCadastro(anyString(), anyString())).thenReturn(Optional.of(customerModel));

        //### When
        final var customerDtoUseCas = loginCustomerUserCase.login(cpf, password);

        //### Then
        verify(iCustomerMapper, times(1)).toDto(any(CustomerModel.class));

        assertThat(customerDtoUseCas).usingRecursiveComparison().isNotNull();

        assertEquals(customerDtoUseCas.getCpf(), customerModel.getCpf());
        assertNotNull(customerDtoUseCas.getCreateAt());
        assertNotNull(customerDtoUseCas.getModifiedAt());
    }

    @Test
    void loginWhenElementNotFoundException() {

        //### Given - Mocks
        when(iSearchUniqueRepositoryPort.findByCadastro(anyString(), anyString())).thenReturn(Optional.empty());

        //### When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> loginCustomerUserCase.login(anyString(), anyString()));

        //### Then
        Assertions.assertEquals(exception.getMessage(), "Incorrect user or password");
        verify(iCustomerMapper, never()).toDto(any(CustomerModel.class));

    }
}