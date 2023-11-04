package com.totem.food.application.usecases.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendImageRequestPort;
import com.totem.food.application.usecases.commons.ICreateImageUseCase;
import lombok.SneakyThrows;
import mock.models.PaymentModelMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateImagePaymentUseCaseTest {

    @Spy
    private ICreateImageUseCase<PaymentDto, byte[]> iCreateImageUseCase;

    @Mock
    private ISendImageRequestPort<PaymentModel> iSendRequest;

    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iCreateImageUseCase = new CreateImagePaymentUseCase(iSendRequest, iPaymentMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }


    @Test
    void createImage() {

        //## Mock - Object
        var paymentDto = PaymentDto.builder()
                .id(UUID.randomUUID().toString())
                .qrcodeBase64(UUID.randomUUID().toString())
                .build();
        var paymentModel = PaymentModelMock.getPaymentStatusPendingMock();
        byte[] byteImage = new byte[]{1, 2, 3};

        //## Given
        when(iPaymentMapper.toModel(paymentDto)).thenReturn(paymentModel);
        when(iSendRequest.sendRequest(paymentModel)).thenReturn(byteImage);

        //## When
        var result = iCreateImageUseCase.createImage(paymentDto);

        //## Then
        assertArrayEquals(byteImage, result);

    }
}