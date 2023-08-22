package com.totem.food.application.ports.in.dtos.order.admin;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class OrderAdminFilterDto {

    private Set<String> status;

    /**
     * Only status: RECEIVED, IN_PREPARATION and READY
     */
    private Boolean onlyTreadmill;

}
