package mocks.adapters.out.web.payment.request;

import com.totem.food.framework.adapters.out.web.payment.entity.PaymentResponseEntity;

public class PaymentResponseEntityMock {

    public static PaymentResponseEntity responseEntity(){
        return new PaymentResponseEntity(
                "qrcodeBase64",
                "qrcode"
        );
    }
}
