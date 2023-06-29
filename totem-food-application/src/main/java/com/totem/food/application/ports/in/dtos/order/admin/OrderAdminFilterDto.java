package com.totem.food.application.ports.in.dtos.order.admin;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class OrderAdminFilterDto {

    private String orderId;
    private Set<String> status;

    public Map<String, String> getFilters(){
        final var map = new HashMap<String, String>();

        if(StringUtils.isNotEmpty(orderId)){
            map.put("orderId", orderId);
        }

        return map;
    }
}
