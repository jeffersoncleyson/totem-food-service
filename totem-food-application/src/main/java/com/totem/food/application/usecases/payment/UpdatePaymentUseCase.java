package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdatePaymentUseCase implements IUpdateUseCase<PaymentFilterDto, Boolean> {

    private final IPaymentMapper iPaymentMapper;
    private final IOrderMapper iOrderMapper;
    private final ISearchRepositoryPort<PaymentFilterDto, PaymentModel> iSearchRepositoryPort;
    private final IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchUniqueRepositoryPort;
    private final IUpdateRepositoryPort<OrderModel> iUpdateOrderRepositoryPort;

    @Override
    public Boolean updateItem(PaymentFilterDto item, String id) {

        final var paymentModel = iSearchRepositoryPort.findAll(item);

        if(Objects.nonNull(paymentModel)){

            final var paymentDomain = iPaymentMapper.toDomain(paymentModel);

            if(paymentDomain.getStatus().equals(PaymentDomain.PaymentStatus.COMPLETED)) return Boolean.TRUE;

            //## Verify Order and Update
            final var orderModel = iSearchUniqueRepositoryPort.findById(item.getOrderId())
                    .orElseThrow(() -> new ElementNotFoundException(
                            String.format("Order with orderId: [%s] not found", item.getOrderId())
                    ));

            final var orderDomain = iOrderMapper.toDomain(orderModel);
            orderDomain.updateOrderStatus(OrderStatusEnumDomain.RECEIVED);
            orderDomain.updateModifiedAt();
            final var orderModelValidated = iOrderMapper.toModel(orderDomain);
            iUpdateOrderRepositoryPort.updateItem(orderModelValidated);

            //## Update Payment
            paymentDomain.updateStatus(PaymentDomain.PaymentStatus.COMPLETED);
            final var paymentModelConverted = iPaymentMapper.toModel(paymentDomain);
            iUpdateRepositoryPort.updateItem(paymentModelConverted);
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