package mocks.domains;

import com.totem.food.domain.payment.PaymentDomain;
import org.bson.types.ObjectId;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class PaymentDomainMock {

    public static PaymentDomain getPaymentDomain(PaymentDomain.PaymentStatus paymentStatus){
        return PaymentDomain.builder()
                .id(new ObjectId().toHexString())
                .order(OrderDomainMock.getStatusNewMock())
                .customer(CustomerDomainMock.getMock())
                .price(50D)
                .token(UUID.randomUUID().toString())
                .status(paymentStatus)
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
