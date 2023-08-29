package com.totem.food.application.ports.out.persistence.order.totem;

import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    @Setter
    private String id;

    @Setter
    private CustomerDomain customer;

    @Setter
    private List<ProductDomain> products;

    @Builder.Default
    private OrderStatusEnumDomain status = OrderStatusEnumDomain.NEW;

    @Setter
    private double price;

    @Setter
    private ZonedDateTime modifiedAt;

    @Setter
    private ZonedDateTime createAt;

    @Setter
    private ZonedDateTime receivedAt;
}
