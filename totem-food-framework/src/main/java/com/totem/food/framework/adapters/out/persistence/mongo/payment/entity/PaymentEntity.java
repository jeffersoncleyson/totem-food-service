package com.totem.food.framework.adapters.out.persistence.mongo.payment.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String status;
    private double price;
    private String token;
    private String qrcodeBase64;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

}
