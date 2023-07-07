package mocks.models;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.payment.PaymentDomain;
import mocks.domains.CustomerDomainMock;
import mocks.domains.OrderDomainMock;
import org.bson.types.ObjectId;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class PaymentModelMock {

    public static PaymentModel getPaymentDomain(PaymentDomain.PaymentStatus paymentStatus){
        return PaymentModel.builder()
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
