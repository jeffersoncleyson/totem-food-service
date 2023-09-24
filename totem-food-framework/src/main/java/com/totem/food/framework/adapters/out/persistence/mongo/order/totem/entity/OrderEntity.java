package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
public class OrderEntity {

    @Id
    private String id;

    private String cpf;

    @DBRef
    private List<ProductEntity> products;

    private String status;

    private double price;

    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
    private ZonedDateTime receivedAt;
}
