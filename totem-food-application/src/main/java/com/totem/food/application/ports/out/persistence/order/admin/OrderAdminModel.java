package com.totem.food.application.ports.out.persistence.order.admin;

import com.totem.food.domain.customer.CustomerDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminModel {

    private String id;
    private double price;
    private CustomerDomain customer;
    private String status;
    private ZonedDateTime createAt;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime receivedAt;
    private long waitTime;
}
