package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;

public class PaymentCreateDtoMock {

    public static PaymentCreateDto getMock() {
        var payment = new PaymentCreateDto();
        payment.setCustomerId("1");
        payment.setOrderId("1");
        return payment;
    }
}
