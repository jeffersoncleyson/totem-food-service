package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
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

    @DBRef
    private CustomerEntity customer;

    @DBRef
    private List<ProductEntity> products;

    @DBRef
    private List<ComboEntity> combos;

    private String status;

    private double price;

    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
