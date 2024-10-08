package com.hampcode.service;

import com.hampcode.dto.PaymentCaptureResponse;
import com.hampcode.dto.PaymentOrderResponse;
import jakarta.mail.MessagingException;

public interface CheckoutService {

    PaymentOrderResponse createPayment(Integer purchaseId, String returnUrl, String cancelUrl) throws MessagingException;

    PaymentCaptureResponse capturePayment(String orderId) throws MessagingException;
}
