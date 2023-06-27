package mock.domain;

import com.totem.food.domain.payment.PaymentDomain;

import java.time.ZonedDateTime;

public class PaymentDomainMock {

    public static PaymentDomain getPaymentStatusPendingMock() {
        return PaymentDomain.builder()
                .id("1")
                .order(OrderDomainMock.getStatusNewMock())
                .customer(CustomerDomainMock.getMock())
                .price(49.99)
                .token("token")
                .status(PaymentDomain.PaymentStatus.PENDING)
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }

    public static PaymentDomain getPaymentStatusCompletedMock() {
        return PaymentDomain.builder()
                .id("1")
                .order(OrderDomainMock.getStatusNewMock())
                .customer(CustomerDomainMock.getMock())
                .price(49.99)
                .token("token")
                .status(PaymentDomain.PaymentStatus.COMPLETED)
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }
}
