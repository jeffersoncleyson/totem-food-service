package mocks.adapters.out.web.payment.request;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.NoArgsConstructor;
import mocks.domains.CustomerDomainMock;
import mocks.domains.OrderDomainMock;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
public class PaymentRequestMock {

    public static PaymentModel paymentDomain(){
        return PaymentModel.builder()
                .id(UUID.randomUUID().toString())
                .order(OrderDomainMock.getStatusNewMock())
                .customer(CustomerDomainMock.getMock())
                .price(50D)
                .token(UUID.randomUUID().toString())
                .status(PaymentDomain.PaymentStatus.COMPLETED)
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
