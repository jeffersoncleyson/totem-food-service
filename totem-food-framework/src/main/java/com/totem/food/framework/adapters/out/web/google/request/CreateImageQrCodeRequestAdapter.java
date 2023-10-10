package com.totem.food.framework.adapters.out.web.google.request;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendImageRequestPort;
import com.totem.food.framework.adapters.out.web.google.client.GoogleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateImageQrCodeRequestAdapter implements ISendImageRequestPort<PaymentModel> {

    // Tamanho da imagem em Pixel
    private static final String IMAGE_WIDTH_HEIGHT = "500x500";

    // Especifica um código QR.
    private static final String IMAGE_TYPE = "qr";

    // Recuperação de até 30% dos dados perdidos.
    private static final String RECOVERY = "H";

    private final GoogleClient googleClient;

    @Override
    public byte[] sendRequest(PaymentModel item) {
        return googleClient.getImageQrCode(IMAGE_WIDTH_HEIGHT, IMAGE_TYPE, RECOVERY, item.getQrcodeBase64());
    }
}
