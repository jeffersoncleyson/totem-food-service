package com.totem.food.domain.order.enums;

import com.totem.food.domain.exceptions.InvalidEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum OrderStatusEnumDomain {

    NEW("NEW"),
    WAITING_PAYMENT("WAITING_PAYMENT"),
    RECEIVED("RECEIVED"),
    IN_PREPARATION("IN_PREPARATION"),
    READY("READY"),
    FINALIZED("FINALIZED"),
    CANCELED("CANCELED");

    public final String key;

    OrderStatusEnumDomain(String key){
        this.key = key;
    }

    public static OrderStatusEnumDomain from(final String source){

        if (StringUtils.isEmpty(source))
            throw new InvalidEnum(source, OrderStatusEnumDomain.getKeys());

        return Arrays.stream(OrderStatusEnumDomain.values())
               .filter(e -> e.key.equalsIgnoreCase(source))
               .findFirst()
               .orElseThrow(() -> new InvalidEnum(source, OrderStatusEnumDomain.getKeys()));
    }

    public static Set<String> getKeys(){
        return Arrays.stream(OrderStatusEnumDomain.values()).map(o -> o.key).collect(Collectors.toSet());
    }
}
