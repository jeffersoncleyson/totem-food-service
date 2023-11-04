package com.totem.food.framework.adapters.in.rest.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Routes {

    //######## VERSIONS
    public static final String API_VERSION_1 = "/v1";

    //######## CATEGORY
    public static final String ADM_CATEGORY = "/administrative/category";
    public static final String CATEGORY_ID = "/{categoryId}";

    //######## CUSTOMER
    public static final String ADM_CUSTOMER = "/administrative/customer";
    public static final String TOTEM_CUSTOMER = "/totem/customer";
    public static final String TOTEM_LOGIN = "/totem/login";
    public static final String CUSTOMER_ID = "/{cpf}";
    public static final String CONFIRM_CUSTOMER = "/code/{code}";

    //######## ORDER
    public static final String ADM_ORDER = "/administrative/orders";
    public static final String TOTEM_ORDER = "/totem/order";
    public static final String ORDER_ID = "/{orderId}";
    public static final String ORDER_ID_AND_STATUS = "/{orderId}/status/{statusName}";

    //######## PAYMENT
    public static final String TOTEM_PAYMENT_CALLBACK = "/totem/payment/callback";
    public static final String PAYMENT_ORDER_ID = "/order/{orderId}";
    public static final String TOTEM_PAYMENT = "/totem/payment";
    public static final String PAYMENT_ID = "/{paymentId}";

    //######## PRODUCT
    public static final String ADM_PRODUCT = "/administrative/product";
    public static final String PRODUCT_ID = "/{productId}";

}
