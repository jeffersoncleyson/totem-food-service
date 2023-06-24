package com.totem.food.framework.adapters.in.rest.order.totem;

import com.totem.food.application.ports.in.dtos.order.totem.*;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateStatusRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.framework.adapters.in.rest.constants.Routes;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = Routes.VERSION_1 + Routes.TOTEM_ORDER)
@AllArgsConstructor
public class TotemOrderRestApiAdapter implements ICreateRestApiPort<OrderCreateDto, ResponseEntity<OrderDto>>,
        ISearchRestApiPort<OrderFilterDto, ResponseEntity<List<OrderDto>>>,
        IUpdateRestApiPort<OrderUpdateDto, ResponseEntity<OrderDto>>,
        IUpdateStatusRestApiPort<ResponseEntity<OrderDto>> {

    private final ICreateUseCase<OrderCreateDto, OrderDto> iCreateUseCase;
    private final ISearchUseCase<OrderFilterDto, List<OrderDto>> iSearchProductUseCase;
    private final IUpdateUseCase<OrderUpdateDto, OrderDto> iUpdateUseCase;
    private final IUpdateStatusUseCase<OrderDto> iUpdateStatusUseCase;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrderDto> create(@RequestBody OrderCreateDto item) {
        final var created = iCreateUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<OrderDto>> listAll(OrderFilterDto filter) {
        return Optional.ofNullable(iSearchProductUseCase.items(filter))
                .filter(CollectionUtils::isNotEmpty)
                .map(items -> ResponseEntity.status(HttpStatus.OK).body(items))
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping(value = Routes.ORDER_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrderDto> update(@RequestBody OrderUpdateDto item, @PathVariable String orderId) {
        return Optional.ofNullable(iUpdateUseCase.updateItem(item, orderId))
                .map( itemDto -> ResponseEntity.status(HttpStatus.ACCEPTED).body(itemDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = Routes.ORDER_ID_AND_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrderDto> update(@PathVariable(name = "orderId") String id, @PathVariable(name = "statusName") String status) {
        final var orderDto = iUpdateStatusUseCase.updateStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }
}