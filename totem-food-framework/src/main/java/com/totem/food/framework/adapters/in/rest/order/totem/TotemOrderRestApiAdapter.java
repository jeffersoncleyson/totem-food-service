package com.totem.food.framework.adapters.in.rest.order.totem;

import com.totem.food.application.ports.in.dtos.context.XUserIdentifierContextDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderCreateDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateStatusRestApiPort;
import com.totem.food.application.usecases.commons.IContextUseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ICreateWithIdentifierUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.ORDER_ID;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.ORDER_ID_AND_STATUS;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_ORDER;

@RestController
@RequestMapping(value = API_VERSION_1 + TOTEM_ORDER)
@AllArgsConstructor
public class TotemOrderRestApiAdapter implements ICreateRestApiPort<OrderCreateDto, ResponseEntity<OrderDto>>,
        ISearchRestApiPort<OrderFilterDto, ResponseEntity<List<OrderDto>>>,
        IUpdateRestApiPort<OrderUpdateDto, ResponseEntity<OrderDto>>,
        IUpdateStatusRestApiPort<ResponseEntity<OrderDto>> {

    private final ICreateWithIdentifierUseCase<OrderCreateDto, OrderDto> iCreateUseCase;
    private final ISearchUseCase<OrderFilterDto, List<OrderDto>> iSearchProductUseCase;
    private final IUpdateUseCase<OrderUpdateDto, OrderDto> iUpdateUseCase;
    private final IUpdateStatusUseCase<OrderDto> iUpdateStatusUseCase;
    private final IContextUseCase<XUserIdentifierContextDto, String> iContextUseCase;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrderDto> create(@RequestBody OrderCreateDto item) {
        final var created = iCreateUseCase.createItem(item, iContextUseCase.getContext());
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

    @PutMapping(value = ORDER_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrderDto> update(@RequestBody OrderUpdateDto item, @PathVariable String orderId) {
        return Optional.ofNullable(iUpdateUseCase.updateItem(item, orderId))
                .map( itemDto -> ResponseEntity.status(HttpStatus.ACCEPTED).body(itemDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = ORDER_ID_AND_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrderDto> update(@PathVariable(name = "orderId") String id, @PathVariable(name = "statusName") String status) {
        final var orderDto = iUpdateStatusUseCase.updateStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }
}