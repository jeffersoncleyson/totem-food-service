package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentElementDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdatePaymentUseCase implements IUpdateUseCase<PaymentFilterDto, Boolean> {

    private final IPaymentMapper iPaymentMapper;
    private final IOrderMapper iOrderMapper;
    private final IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchOrderModel;
    private final IUpdateRepositoryPort<OrderModel> iUpdateOrderRepositoryPort;
    private final ISearchRepositoryPort<PaymentFilterDto, List<PaymentModel>> iSearchRepositoryPort;
    private final ISendRequestPort<String, PaymentElementDto> iSendRequest;

    @Override
    public Boolean updateItem(PaymentFilterDto item, String id) {

        var paymentsModel = iSearchRepositoryPort.findAll(item);

        if (ObjectUtils.isEmpty(paymentsModel)) {
            return Boolean.FALSE;
        }

        //## Search for payments in the partner and update payment in the database
        for (PaymentModel paymentModel : paymentsModel) {

            var paymentElementDto = iSendRequest.sendRequest(paymentModel.getId());

            if (Objects.nonNull(paymentElementDto) && paymentElementDto.getOrderStatus().equals("paid")) {

                final var paymentDomain = iPaymentMapper.toDomain(paymentModel);

                final var orderModel = iSearchOrderModel.findById(paymentModel.getOrder().getId())
                        .orElseThrow(() -> new ElementNotFoundException(String.format("Order with orderId: [%s] not found", paymentModel.getOrder().getId())));

                if (verifyOrderPaid(paymentDomain, orderModel)) {
                    updateOrderReceived(orderModel);
                    updatePaymentCompleted(paymentDomain);
                }
            }
        }
        return Boolean.TRUE;
    }

    //## Verify Order is Received and Payment is Completed
    private static boolean verifyOrderPaid(PaymentDomain paymentDomain, OrderModel orderModel) {
        return !paymentDomain.getStatus().equals(PaymentDomain.PaymentStatus.COMPLETED) && !orderModel.getStatus().equals(OrderStatusEnumDomain.RECEIVED);
    }

    //## Update Payment
    private void updatePaymentCompleted(PaymentDomain paymentDomain) {
        paymentDomain.updateStatus(PaymentDomain.PaymentStatus.COMPLETED);
        final var paymentModelConverted = iPaymentMapper.toModel(paymentDomain);
        iUpdateRepositoryPort.updateItem(paymentModelConverted);
    }

    //## Update Order
    private void updateOrderReceived(OrderModel orderModel) {
        final var orderDomain = iOrderMapper.toDomain(orderModel);
        orderDomain.updateOrderStatus(OrderStatusEnumDomain.RECEIVED);
        orderDomain.updateModifiedAt();
        final var orderModelValidated = iOrderMapper.toModel(orderDomain);
        iUpdateOrderRepositoryPort.updateItem(orderModelValidated);
    }
}