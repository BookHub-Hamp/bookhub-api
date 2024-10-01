package com.hampcode.paypal.service;

import com.hampcode.paypal.dto.OrderCaptureResponse;
import com.hampcode.paypal.dto.OrderResponse;

public interface PayPalService {

    String getAccessToken();

    OrderResponse createOrder(Integer purchaseId, String returnUrl, String cancelUrl);

    OrderCaptureResponse captureOrder(String orderId);
}
