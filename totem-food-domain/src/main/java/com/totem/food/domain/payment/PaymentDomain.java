package com.totem.food.domain.payment;

import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDomain {

    private String id;
    private OrderDomain order;
    private CustomerDomain customer;
    private double price;
    private String token;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

    public void updateModifiedAt(){
        this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    public void fillDates(){
        if(StringUtils.isEmpty(this.id)){
            this.createAt = ZonedDateTime.now(ZoneOffset.UTC);
            this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        }
    }
}
