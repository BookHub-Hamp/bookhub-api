package com.hampcode.paypal.dto;

import lombok.Data;

@Data
public class PaypalCaptureResponse {
    private boolean completed;
    private Integer purchaseId;
}