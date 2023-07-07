package com.totem.food.framework.adapters.out.persistence.mongo.order.admin.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
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
@Document(collection = "order")
public class OrderAdminEntity {

    @Id
    private String id;
    private double price;
    @DBRef
    private CustomerEntity customer;
    private String status;
    private ZonedDateTime createAt;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime receivedAt;

}
