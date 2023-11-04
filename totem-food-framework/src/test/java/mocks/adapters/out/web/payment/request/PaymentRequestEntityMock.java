package mocks.adapters.out.web.payment.request;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.framework.adapters.in.rest.payment.entity.PaymentItemsRequestEntity;
import com.totem.food.framework.adapters.in.rest.payment.entity.PaymentRequestEntity;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class PaymentRequestEntityMock {

    private static final ZonedDateTime DURATION_QR_CODE = ZonedDateTime.now().plusHours(1);

    public static PaymentRequestEntity getPaymentRequestEntity(PaymentModel item) {
        return PaymentRequestEntity.builder()
                .externalReference(item.getId())
                .totalAmount(BigDecimal.valueOf(item.getPrice()))
                .items(getItemsRequest(item))
                .title("Atendimento via Totem")
                .description("Pedido realizado via auto atendimento Totem")
                .expirationDate(DURATION_QR_CODE)
                .notificationUrl(UUID.randomUUID().toString())
                .build();
    }

    private static List<PaymentItemsRequestEntity> getItemsRequest(PaymentModel item) {
        return List.of(PaymentItemsRequestEntity.builder()
                .skuNumber(item.getId())
                .category("Alimentos")
                .title("Totem Food Service")
                .description("Pedido via Totem")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(item.getPrice()))
                .unitMeasure(String.valueOf(item.getPrice()))
                .totalAmount(BigDecimal.valueOf(item.getPrice()))
                .build());
    }
}
