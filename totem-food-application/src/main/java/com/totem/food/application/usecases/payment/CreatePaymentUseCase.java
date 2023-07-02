package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.exceptions.InvalidStatusException;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@UseCase
public class CreatePaymentUseCase implements ICreateUseCase<PaymentCreateDto, PaymentQRCodeDto> {

    private final ICreateRepositoryPort<PaymentModel> iCreateRepositoryPort;
    private final ICustomerMapper iCustomerMapper;
    private final IPaymentMapper iPaymentMapper;
    private final ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueOrderRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueCustomerRepositoryPort;
    private final ISendRequestPort<PaymentModel, PaymentQRCodeDto> iSendRequest;

    @Override
    public PaymentQRCodeDto createItem(PaymentCreateDto item) {

        final var orderDomain = iSearchUniqueOrderRepositoryPort.findById(item.getOrderId())
                .orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] not found", item.getOrderId())));

        if(orderDomain.getStatus().equals(OrderStatusEnumDomain.WAITING_PAYMENT)) {
            final var paymentDomainBuilder = PaymentDomain.builder();

            Optional.ofNullable(item.getCustomerId())
                    .filter(StringUtils::isNotEmpty)
                    .ifPresent(customerId -> {
                        final var customerModel = iSearchUniqueCustomerRepositoryPort.findById(customerId)
                                .orElseThrow(() -> new ElementNotFoundException(String.format("Customer [%s] not found", customerId)));
                        final var customerDomain = iCustomerMapper.toDomain(customerModel);
                        paymentDomainBuilder.customer(customerDomain);
                    });

            paymentDomainBuilder.order(orderDomain);
            paymentDomainBuilder.price(orderDomain.getPrice());
            paymentDomainBuilder.token(UUID.randomUUID().toString());
            paymentDomainBuilder.status(PaymentDomain.PaymentStatus.PENDING);


            final var paymentDomain = paymentDomainBuilder.build();
            paymentDomain.fillDates();

            final var paymentModel = iPaymentMapper.toModel(paymentDomain);
            final var paymentDomainSaved = iCreateRepositoryPort.saveItem(paymentModel);
            final var paymentDto = iSendRequest.sendRequest(paymentDomainSaved);
            paymentDto.setStatus(PaymentDomain.PaymentStatus.PENDING.key);
            paymentDto.setPaymentId(paymentDomainSaved.getId());
            return paymentDto;
        }

        throw new InvalidStatusException("Order", orderDomain.getStatus().key, OrderStatusEnumDomain.WAITING_PAYMENT.key);
    }

}