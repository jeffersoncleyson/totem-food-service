package mock.models;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.payment.PaymentDomain;
import mock.domain.CustomerDomainMock;
import mock.domain.OrderDomainMock;

import java.time.ZonedDateTime;

public class PaymentModelMock {

    public static PaymentModel getPaymentStatusPendingMock() {
        return PaymentModel.builder()
                .id("1")
                .order(OrderDomainMock.getStatusWaitingPaymentMock())
                .customer(CustomerDomainMock.getMock())
                .price(49.99)
                .token("token")
                .status(PaymentDomain.PaymentStatus.PENDING)
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }

    public static PaymentModel getPaymentStatusCompletedMock() {
        return PaymentModel.builder()
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
