package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.domain.payment.PaymentDomain;

public class PaymentQRCodeDtoMock {

    public static PaymentQRCodeDto getStatusPendingMock() {
        var payment = new PaymentQRCodeDto();
        payment.setPaymentId("1");
        payment.setStoreOrderId("qrCode");
        payment.setQrcodeBase64("base64-QrCode");
        payment.setStatus(PaymentDomain.PaymentStatus.PENDING.key);
        return payment;
    }
}
