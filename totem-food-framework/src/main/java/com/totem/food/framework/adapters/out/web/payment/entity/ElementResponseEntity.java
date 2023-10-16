package com.totem.food.framework.adapters.out.web.payment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementResponseEntity {

    @JsonProperty("elements")
    private List<ElementDataResponseEntity> data;

}
