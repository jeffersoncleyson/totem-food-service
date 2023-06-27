package mock.domain;

import com.totem.food.domain.payment.PaymentDomain;

import java.time.ZonedDateTime;

public class PaymentDomainMock {

    public static PaymentDomain getMock() {
        var paymentDomain = new PaymentDomain();
        paymentDomain.setId("1");
        paymentDomain.setOrder(OrderDomainMock.getStatusNewMock());
        paymentDomain.setCustomer(CustomerDomainMock.getMock());
        paymentDomain.setPrice(49.99);
        paymentDomain.setToken("token");
        paymentDomain.setStatus(PaymentDomain.PaymentStatus.PENDING);
        paymentDomain.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        paymentDomain.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return paymentDomain;
    }
}
