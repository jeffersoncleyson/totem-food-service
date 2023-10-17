package com.totem.food.framework.adapters.out.web.payment.client;

import com.totem.food.framework.adapters.in.rest.payment.entity.PaymentRequestEntity;
import com.totem.food.framework.adapters.out.web.payment.entity.ElementResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.entity.PaymentResponseEntity;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MercadoPagoAPI", url = "${payment.url}")
@Headers({
        "Content-Type: application/json; charset=utf-8",
        "Accept: application/json; charset=utf-8"
})
public interface MercadoPagoClient {

    @PostMapping(value = "/instore/orders/qr/seller/collectors/{user_id}/pos/{pos_id}/qrs")
    ResponseEntity<PaymentResponseEntity> createOrder(@RequestHeader(name = "Authorization") String authorization,
                                                      @PathVariable(name = "user_id") String userId,
                                                      @PathVariable(name = "pos_id") String posId,
                                                      @RequestBody PaymentRequestEntity paymentRequestEntity);

    @GetMapping(value = "/merchant_orders", produces = "application/json")
    ResponseEntity<ElementResponseEntity> getOrderDetails(@RequestHeader(name = "Authorization") String token,
                                                          @RequestParam("external_reference") String externalReference);
}
