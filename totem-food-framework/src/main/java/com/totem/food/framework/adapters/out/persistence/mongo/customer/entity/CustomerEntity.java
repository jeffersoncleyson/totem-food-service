package com.totem.food.framework.adapters.out.persistence.mongo.customer.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer")
public class CustomerEntity {

    @Id
    private String id;
    private String name;
    private String cpf;
    private String email;
    private String mobile;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
