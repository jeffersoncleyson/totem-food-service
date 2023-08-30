package com.totem.food.application.usecases.customer;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUserCaseTest {

    @Spy
    private ICustomerMapper iCustomerMapper = Mappers.getMapper(ICustomerMapper.class);

    @Mock
    private ICreateRepositoryPort<CustomerModel> iCreateRepositoryPort;

    @Mock
    private IExistsRepositoryPort<CustomerModel, Boolean> iExistsRepositoryPort;

    private CreateCustomerUserCase createCustomerUserCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createCustomerUserCase = new CreateCustomerUserCase(iCustomerMapper, iCreateRepositoryPort, iExistsRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void createItem() {

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

        final var customerCreateDto = new CustomerCreateDto();
        customerCreateDto.setName(name);
        customerCreateDto.setEmail(email);
        customerCreateDto.setCpf(cpf);
        customerCreateDto.setMobile(mobile);
        customerCreateDto.setPassword(password);

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
        when(iExistsRepositoryPort.exists(Mockito.any())).thenReturn(false);
        when(iCreateRepositoryPort.saveItem(Mockito.any(CustomerModel.class))).thenReturn(customerModel);

        //### When
        final var customerDtoUseCase = createCustomerUserCase.createItem(customerCreateDto);

        //### Then
        verify(iCustomerMapper, times(1)).toDomain(Mockito.any(CustomerCreateDto.class));
        verify(iExistsRepositoryPort, times(1)).exists(Mockito.any());
        verify(iCreateRepositoryPort, times(1)).saveItem(Mockito.any(CustomerModel.class));
        verify(iCustomerMapper, times(1)).toDto(Mockito.any(CustomerModel.class));

        assertThat(customerDtoUseCase).usingRecursiveComparison().isNotNull();

        assertEquals(customerDtoUseCase.getCpf(), customerCreateDto.getCpf());
        assertNotNull(customerDtoUseCase.getCreateAt());
        assertNotNull(customerDtoUseCase.getModifiedAt());
    }

    @Test
    void createItemWhenElementExistsException() {

        //### Given - Objects and Values
        final var name = "John";
        final var email = "john@gmail.com";
        final var cpf = "51397955074";
        final var mobile = "5511900112233";
        final var password = "%#AjOBF%w.<K";

        final var customerCreateDto = new CustomerCreateDto();
        customerCreateDto.setName(name);
        customerCreateDto.setEmail(email);
        customerCreateDto.setCpf(cpf);
        customerCreateDto.setMobile(mobile);
        customerCreateDto.setPassword(password);

        //### Given - Mocks
        when(iExistsRepositoryPort.exists(Mockito.any())).thenReturn(true);

        //### When
        assertThrows(ElementExistsException.class, () -> createCustomerUserCase.createItem(customerCreateDto));

        //### Then
        verify(iCreateRepositoryPort, never()).saveItem(Mockito.any(CustomerModel.class));
        verify(iCustomerMapper, never()).toDto(Mockito.any(CustomerModel.class));

    }
}