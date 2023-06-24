package com.totem.food.domain.order.admin;

import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
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
    private ZonedDateTime receivedAt;
    private long waitTime;

    public void calcWaitTime(){
        if(isOrderInProgress()){
            final var duration = Duration.between( receivedAt , ZonedDateTime.now(ZoneOffset.UTC));
            this.waitTime = duration.toMinutes();
        } else if(isOrderFinalized()){
            final var duration = Duration.between( receivedAt , modifiedAt);
            this.waitTime = duration.toMinutes();
        } else {
            this.waitTime = 0;
        }
    }

    private boolean isOrderFinalized() {
        return Objects.nonNull(receivedAt) && Objects.nonNull(modifiedAt) && this.status.equals(OrderStatusEnumDomain.FINALIZED.key);
    }

    private boolean isOrderInProgress(){
        return Objects.nonNull(receivedAt) &&
                (this.status.equals(OrderStatusEnumDomain.RECEIVED.key) ||
                this.status.equals(OrderStatusEnumDomain.IN_PREPARATION.key));
    }

}
