package mocks.dtos;

import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
public class PaymentMocks {

    public static PaymentCreateDto paymentCreateDto(String orderId, String customerId) {
        return new PaymentCreateDto(
                orderId,
                customerId
        );
    }

    public static PaymentQRCodeDto paymentQRCodeDto(String paymentId, String status) {
        return new PaymentQRCodeDto(
                "qrcodeBase64",
                "qrcode",
                status,
                paymentId
        );
    }

    public static PaymentDto paymentDto(){
        return new PaymentDto(
                UUID.randomUUID().toString(),
                25.0D,
                UUID.randomUUID().toString(),
                PaymentDomain.PaymentStatus.COMPLETED.key,
                ZonedDateTime.now(ZoneOffset.UTC),
                ZonedDateTime.now(ZoneOffset.UTC)
        );
    }

}
