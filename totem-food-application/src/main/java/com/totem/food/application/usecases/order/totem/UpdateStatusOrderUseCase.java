package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import com.totem.food.application.ports.out.email.ISendEmailPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdateStatusOrderUseCase implements IUpdateStatusUseCase<OrderDto> {

    private final IOrderMapper iOrderMapper;
    private final ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;
    private final IUpdateRepositoryPort<OrderDomain> iProductRepositoryPort;
    private final ISendEmailPort<EmailNotificationDto, Boolean> iSendEmailPort;

    @Override
    public OrderDto updateStatus(String id, String status) {

        final var orderDomainOpt = iSearchUniqueRepositoryPort.findById(id);

        final var domain = orderDomainOpt.orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] not found", id)));

        if (domain.getStatus().equals(OrderStatusEnumDomain.from(status))) {
            return iOrderMapper.toDto(domain);
        }

        domain.updateOrderStatus(OrderStatusEnumDomain.from(status));
        domain.updateModifiedAt();
        final var domainSaved = iProductRepositoryPort.updateItem(domain);

        if (domainSaved.getStatus().equals(OrderStatusEnumDomain.READY)) {
            Optional.of(domainSaved).map(OrderDomain::getCustomer).map(CustomerDomain::getEmail)
                    .ifPresent(email -> {
                        final var subject = String.format("[%s] Pedido %s", "Totem Food Service", id);
                        final var message = String.format("Pedido %s acabou de ser finalizado pela cozinha, em instantes o atendente ira chama-lo!", id);
                        iSendEmailPort.sendEmail(new EmailNotificationDto(email, subject, message));
                    });
        }

        return iOrderMapper.toDto(domainSaved);
    }

}