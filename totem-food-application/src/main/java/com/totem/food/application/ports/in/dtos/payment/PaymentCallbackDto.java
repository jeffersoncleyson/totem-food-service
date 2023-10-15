package com.totem.food.application.ports.in.dtos.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentCallbackDto {

    @JsonProperty("action")
    private String action;

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("application_id")
    private String applicationId;

    @JsonProperty("date_created")
    private String dateCreated;

    @JsonProperty("id")
    private String paymentId;

    @JsonProperty("live_mode")
    private Boolean liveMode;

    @JsonProperty("type")
    private String type;

    @JsonProperty("user_id")
    private String sellerId;

    @JsonProperty("data")
    private PaymentDataDto data;
}
