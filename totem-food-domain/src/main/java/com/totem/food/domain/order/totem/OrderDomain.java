package com.totem.food.domain.order.totem;

import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.*;

import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDomain {

    @Getter @Setter
    private CustomerDomain customer;

    @Getter @Setter
    private List<ProductDomain> products;

    @Getter @Setter
    private List<ComboDomain> combos;

    @Getter
    @Builder.Default
    private OrderStatusEnumDomain status = OrderStatusEnumDomain.NEW;

    public void updateOrderStatus(OrderStatusEnumDomain status){

        final var statusTransition = StatusTransition.from(status.key).orElseThrow();

        if(!Objects.equals(status.key, statusTransition.key) && statusTransition.allowedTransitions().contains(status)){
            this.status = status;
        }
    }

    private enum StatusTransition {

        NEW("NEW") {
            @Override
            public Set<OrderStatusEnumDomain> allowedTransitions() {
                return Set.of(OrderStatusEnumDomain.CANCELED, OrderStatusEnumDomain.RECEIVED);
            }
        },
        RECEIVED("RECEIVED") {
            @Override
            public Set<OrderStatusEnumDomain> allowedTransitions() {
                return Set.of(OrderStatusEnumDomain.IN_PREPARATION);
            }
        },
        IN_PREPARATION("IN_PREPARATION") {
            @Override
            public Set<OrderStatusEnumDomain> allowedTransitions() {
                return Set.of(OrderStatusEnumDomain.READY);
            }
        },
        READY("READY") {
            @Override
            public Set<OrderStatusEnumDomain> allowedTransitions() {
                return Set.of(OrderStatusEnumDomain.FINALIZED);
            }
        },
        CANCELED("CANCELED") {
            @Override
            public Set<OrderStatusEnumDomain> allowedTransitions() {
                return Set.of(OrderStatusEnumDomain.CANCELED);
            }
        },
        UNKNOWN("UNKNOWN") {
            @Override
            public Set<OrderStatusEnumDomain> allowedTransitions() {
                return Set.of(OrderStatusEnumDomain.UNKNOWN);
            }
        };

        public final String key;

        StatusTransition(String key){
            this.key = key;
        }


        public abstract Set<OrderStatusEnumDomain> allowedTransitions();

        public static Optional<StatusTransition> from(final String source){
            if (source == null) return Optional.of(UNKNOWN);
            return Arrays.stream(StatusTransition.values()).filter(e -> e.key.equalsIgnoreCase(source)).findFirst();
        }

    }
}
