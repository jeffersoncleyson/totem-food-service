package mocks.entity;

import com.totem.food.framework.adapters.out.web.payment.entity.ElementDataResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.entity.ElementPaymentResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.entity.ElementResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public class ElementResponseEntityMock {

    public static ElementResponseEntity getElementResponseEntity() {
        return ElementResponseEntity.builder()
                .data(List.of(ElementDataResponseEntity.builder()
                        .externalReference("12345")
                        .totalPayment(BigDecimal.TEN)
                        .payments(List.of(ElementPaymentResponseEntity.builder()
                                .status("approved")
                                .build()))
                        .orderStatus("paid")
                        .build()))
                .build();

    }


}
