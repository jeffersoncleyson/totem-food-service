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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@UseCase
public class UpdatePaymentUseCase implements IUpdateUseCase<PaymentFilterDto, Boolean> {

    private final IPaymentMapper iPaymentMapper;
    private final IOrderMapper iOrderMapper;
    private final IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchOrderModel;
    private final ISearchUniqueRepositoryPort<Optional<PaymentModel>> iSearchPaymentModel;
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

            if (paymentElementDto.getOrderStatus().equals("paid")) {

                final var paymentDomain = iPaymentMapper.toDomain(paymentModel);

                final var orderModel = iSearchOrderModel.findById(paymentModel.getOrder().getId())
                        .orElseThrow(() -> new ElementNotFoundException(String.format("Order with orderId: [%s] not found", paymentModel.getOrder().getId())));

                //## Verify Order is Received and Payment is Completed
                if (paymentDomain.getStatus().equals(PaymentDomain.PaymentStatus.COMPLETED) && orderModel.getStatus().equals(OrderStatusEnumDomain.RECEIVED)) {
                    log.info(String.format("Order %s is received with Payment %s completed", orderModel.getStatus(), paymentDomain.getStatus()));
                }

                //## Order Update
                updateOrderReceived(orderModel);

                //## Update Payment
                paymentDomain.updateStatus(PaymentDomain.PaymentStatus.COMPLETED);
                final var paymentModelConverted = iPaymentMapper.toModel(paymentDomain);
                iUpdateRepositoryPort.updateItem(paymentModelConverted);
            }
        }
        return Boolean.TRUE;
    }

    private void updateOrderReceived(OrderModel orderModel) {
        final var orderDomain = iOrderMapper.toDomain(orderModel);
        orderDomain.updateOrderStatus(OrderStatusEnumDomain.RECEIVED);
        orderDomain.updateModifiedAt();
        final var orderModelValidated = iOrderMapper.toModel(orderDomain);
        iUpdateOrderRepositoryPort.updateItem(orderModelValidated);
    }
}