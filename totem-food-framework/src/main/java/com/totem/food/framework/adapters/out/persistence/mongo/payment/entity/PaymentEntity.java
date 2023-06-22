package com.totem.food.framework.adapters.out.persistence.mongo.payment.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment")
public class PaymentEntity {

    @Id
    private String id;
    @DBRef
    private OrderEntity order;
    @DBRef
    private CustomerEntity customer;
    private double price;
    private String token;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
