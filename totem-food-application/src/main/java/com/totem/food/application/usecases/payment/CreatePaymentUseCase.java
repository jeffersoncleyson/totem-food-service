package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.web.ISendRequest;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@UseCase
public class CreatePaymentUseCase implements ICreateUseCase<PaymentCreateDto, PaymentDto> {

    private final ICreateRepositoryPort<PaymentDomain> iCreateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueOrderRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CustomerDomain>> iSearchUniqueCustomerRepositoryPort;
    private final ISendRequest<PaymentDomain, PaymentDto> iSendRequest;

    @Override
    public PaymentDto createItem(PaymentCreateDto item) {

        final var orderDomain = iSearchUniqueOrderRepositoryPort.findById(item.getOrderId())
                .orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] not found", item.getOrderId())));
        final var customerDomain = iSearchUniqueCustomerRepositoryPort.findById(item.getCustomerId())
                .orElseThrow(() -> new ElementNotFoundException(String.format("Customer [%s] not found", item.getCustomerId())));

        final var paymentDomain = PaymentDomain.builder()
                .order(orderDomain)
                .customer(customerDomain)
                .price(orderDomain.getPrice())
                .token(UUID.randomUUID().toString())
                .build();
        paymentDomain.fillDates();

        final var paymentDomainSaved = iCreateRepositoryPort.saveItem(paymentDomain);
        return iSendRequest.sendRequest(paymentDomainSaved);
    }

}