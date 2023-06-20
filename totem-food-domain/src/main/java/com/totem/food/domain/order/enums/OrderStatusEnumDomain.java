package com.totem.food.domain.order.enums;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatusEnumDomain {

    NEW("NEW"),
    RECEIVED("RECEIVED"),
    IN_PREPARATION("IN_PREPARATION"),
    READY("READY"),
    FINALIZED("FINALIZED"),
    CANCELED("CANCELED"),
    UNKNOWN("UNKNOWN");


    public final String key;

    OrderStatusEnumDomain(String key){
        this.key = key;
    }

    public static Optional<OrderStatusEnumDomain> from(final String source){
        if (source == null) return Optional.of(UNKNOWN);
        return Arrays.stream(OrderStatusEnumDomain.values()).filter(e -> e.key.equalsIgnoreCase(source)).findFirst();
    }
}
