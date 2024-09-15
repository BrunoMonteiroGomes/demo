package com.example.paymentprocessor.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentItemDTO {

    @JsonProperty("payment_id")
    private String paymentId;
    @JsonProperty("payment_value")
    private BigDecimal paymentValue;
    @JsonProperty("payment_status")
    private String paymentStatus;
}




