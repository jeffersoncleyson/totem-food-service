package com.totem.food.application.ports.in.dtos.order;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class OrderFilterDto {

    private String orderId;

    public Map<String, String> getFilters(){
        final var map = new HashMap<String, String>();

        if(StringUtils.isNotEmpty(orderId)){
            map.put("orderId", orderId);
        }

        return map;
    }
}
