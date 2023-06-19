package com.totem.food.domain.order.admin;

import com.totem.food.domain.customer.CustomerDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminDomain {

    //########### Main Fields
    private String orderId;
    private Integer showNumber;
    private BigDecimal amount;
    private CustomerDomain customer;
    private ZonedDateTime createAt;

}
