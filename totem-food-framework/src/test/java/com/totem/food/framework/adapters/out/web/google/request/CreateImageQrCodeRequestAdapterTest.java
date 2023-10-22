package com.totem.food.framework.adapters.out.web.google.request;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendImageRequestPort;
import com.totem.food.framework.adapters.out.web.google.client.GoogleClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateImageQrCodeRequestAdapterTest {

    private ISendImageRequestPort<PaymentModel> iSendImageRequestPort;

    @Spy
    private GoogleClient googleClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iSendImageRequestPort = new CreateImageQrCodeRequestAdapter(googleClient);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void testSendRequestWhenValidPaymentModelThenReturnByteArray() {

        //## Mocks - Objects
        var paymentModel = new PaymentModel();
        paymentModel.setQrcodeBase64("00020101021243650016COM.MERCADOLIBRE020130636261f1640-ca77...Test6009SAO PAULO62070503***6304E05E");
        byte[] response = new byte[]{1, 2, 3};

        //## Given
        when(googleClient.getImageQrCode(any(), any(), any(), any())).thenReturn(response);

        //## When
        byte[] result = iSendImageRequestPort.sendRequest(paymentModel);

        //## Then
        assertArrayEquals(response, result);
    }

}