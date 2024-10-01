package com.hampcode.service;

import com.hampcode.paypal.dto.PaypalCaptureResponse;
import com.hampcode.paypal.dto.PaypalOrderResponse;

public interface CheckoutService {

    PaypalOrderResponse createPaypalPaymentUrl(Integer purchaseId, String returnUrl, String cancelUrl);

    PaypalCaptureResponse capturePaypalPayment(String orderId);
}