package com.totem.food.framework.adapters.out.persistence.mongo.combo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "combo")
public class ComboEntity {

    @Id
    private String id;
    private String name;
    private BigDecimal price;
    private List<String> products;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

}
