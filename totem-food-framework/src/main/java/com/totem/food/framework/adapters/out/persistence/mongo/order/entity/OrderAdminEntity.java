package com.totem.food.framework.adapters.out.persistence.mongo.order.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
public class OrderAdminEntity {

    @Id
    private String id;
    private String orderId;
    private Integer showNumber;
    private BigDecimal amount;

    @DBRef
    private CustomerEntity customer;

    private ZonedDateTime createAt;

}
