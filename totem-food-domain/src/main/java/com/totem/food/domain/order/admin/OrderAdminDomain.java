package com.totem.food.domain.order.admin;

import com.totem.food.domain.customer.CustomerDomain;
import lombok.*;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminDomain {

    //########### Main Fields
    private String id;
    private double price;
    private CustomerDomain customer;
    private String status;
    private ZonedDateTime createAt;
    private ZonedDateTime modifiedAt;
    private long waitTime;

    public void calcWaitTime(){
        if(Objects.nonNull(createAt)){
            final var duration = Duration.between( createAt , ZonedDateTime.now(ZoneOffset.UTC));
            this.waitTime = duration.toMinutes();
        }
    }

}
