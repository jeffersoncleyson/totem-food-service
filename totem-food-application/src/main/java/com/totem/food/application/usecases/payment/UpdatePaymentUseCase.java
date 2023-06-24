package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdatePaymentUseCase implements IUpdateUseCase<PaymentFilterDto, Boolean> {

    private final ISearchRepositoryPort<PaymentFilterDto, PaymentDomain> iSearchRepositoryPort;
    private final IUpdateRepositoryPort<PaymentDomain> iUpdateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;
    private final IUpdateRepositoryPort<OrderDomain> iUpdateOrderRepositoryPort;

    @Override
    public Boolean updateItem(PaymentFilterDto item, String id) {

        final var paymentDomain = iSearchRepositoryPort.findAll(item);

        if(Objects.nonNull(paymentDomain)){

            if(paymentDomain.getStatus().equals(PaymentDomain.PaymentStatus.COMPLETED)) return Boolean.TRUE;

            //## Verify Order and Update
            final var orderDomain = iSearchUniqueRepositoryPort.findById(item.getOrderId())
                    .orElseThrow(() -> new ElementNotFoundException(
                            String.format("Order with orderId: [%s] not found", item.getOrderId())
                    ));
            orderDomain.updateOrderStatus(OrderStatusEnumDomain.RECEIVED);
            orderDomain.updateModifiedAt();
            iUpdateOrderRepositoryPort.updateItem(orderDomain);

            //## Update Payment
            paymentDomain.updateStatus(PaymentDomain.PaymentStatus.COMPLETED);
            iUpdateRepositoryPort.updateItem(paymentDomain);
            return Boolean.TRUE;
        }

        throw new ElementNotFoundException(
                String.format("Payment with filters orderId: [%s] token: [%s] not found",
                        item.getOrderId(),
                        item.getToken()
                )
        );
    }
}