package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import com.totem.food.application.ports.out.email.ISendEmailPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@AllArgsConstructor
@UseCase
public class UpdateStatusOrderUseCase implements IUpdateStatusUseCase<OrderDto> {

    private final IOrderMapper iOrderMapper;
    private final ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchUniqueRepositoryPort;
    private final IUpdateRepositoryPort<OrderModel> iProductRepositoryPort;
    private final ISendEmailPort<EmailNotificationDto, Boolean> iSendEmailPort;
    private final ISearchRepositoryPort<PaymentFilterDto, List<PaymentModel>> iSearchPaymentRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueCustomerRepositoryPort;

    @Override
    public OrderDto updateStatus(String id, String status) {

        final var orderModelOptional = iSearchUniqueRepositoryPort.findById(id);

        final var model = orderModelOptional.orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] not found", id)));

        if (model.getStatus().equals(OrderStatusEnumDomain.from(status))) {
            return iOrderMapper.toDto(model);
        }

        final var domain = iOrderMapper.toDomain(model);

        domain.updateOrderStatus(OrderStatusEnumDomain.from(status));
        domain.updateModifiedAt();

        if (domain.getStatus().equals(OrderStatusEnumDomain.RECEIVED)) {

            final var paymentFilter = PaymentFilterDto.builder().orderId(domain.getId()).status(PaymentDomain.PaymentStatus.COMPLETED.key).build();
            Optional.ofNullable(iSearchPaymentRepositoryPort.findAll(paymentFilter))
                    .filter(List::isEmpty)
                    .orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] needs a payment request or Payment is PENDING", id)));

        }

        final var domainValidated = iOrderMapper.toModel(domain);
        final var domainSaved = iProductRepositoryPort.updateItem(domainValidated);

        if (domainSaved.getStatus().equals(OrderStatusEnumDomain.READY)) {
            Optional.of(domainSaved)
                    .map(OrderModel::getCustomer)
                    .map(CustomerDomain::getCpf)
                    .flatMap(cpf -> iSearchUniqueCustomerRepositoryPort.findById(cpf)
                            .map(CustomerModel::getEmail)).ifPresent(sendEmail(id));
        }

        return iOrderMapper.toDto(domainSaved);
    }

    private Consumer<String> sendEmail(String id) {
        return email -> {
            final var subject = String.format("[%s] Pedido %s", "Totem Food Service", id);
            final var message = String.format("Pedido %s acabou de ser finalizado pela cozinha, em instantes o atendente ira chama-lo!", id);
            iSendEmailPort.sendEmail(new EmailNotificationDto(email, subject, message));
        };
    }

}