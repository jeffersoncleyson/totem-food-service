package com.totem.food.application.ports.in.dtos.order.admin;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminDto {

    private String id;
    private double price;
    private CustomerDto customer;
    private String status;
    private ZonedDateTime createAt;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime receivedAt;
    private long waitTime;
}
