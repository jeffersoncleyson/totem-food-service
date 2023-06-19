package com.totem.food.framework.adapters.in.rest.order;

import com.totem.food.application.ports.in.dtos.order.OrderAdminDto;
import com.totem.food.application.ports.in.dtos.order.OrderFilterDto;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/administrative/orders")
@AllArgsConstructor
public class AdministrativeOrderRestApiAdapter implements ISearchRestApiPort<OrderFilterDto, ResponseEntity<List<OrderAdminDto>>> {

    private final ISearchUseCase<OrderFilterDto, List<OrderAdminDto>> iSearchOrderUseCase;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<OrderAdminDto>> listAll(OrderFilterDto filter) {
        final var orders = iSearchOrderUseCase.items(filter);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
