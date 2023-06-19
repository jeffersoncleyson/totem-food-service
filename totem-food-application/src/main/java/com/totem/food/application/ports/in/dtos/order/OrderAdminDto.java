package com.totem.food.application.ports.in.dtos.order;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminDto {

    private String number;
    private BigDecimal amout;
    private CustomerDto description;
    private ZonedDateTime createAt;
}
