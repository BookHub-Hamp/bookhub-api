package com.hampcode.api;

import com.hampcode.paypal.dto.PaypalCaptureResponse;
import com.hampcode.paypal.dto.PaypalOrderResponse;
import com.hampcode.service.CheckoutService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
@PreAuthorize("hasRole('CUSTOMER')")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/paypal/create")
    public ResponseEntity<PaypalOrderResponse> createPaypalOrder(
            @RequestParam Integer purchaseId,
            @RequestParam String returnUrl,
            @RequestParam String cancelUrl
    ) {
        PaypalOrderResponse response = checkoutService.createPaypalPaymentUrl(purchaseId, returnUrl, cancelUrl);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/paypal/capture")
    public ResponseEntity<PaypalCaptureResponse> capturePaypalOrder(@RequestParam String orderId) {
        PaypalCaptureResponse response = checkoutService.capturePaypalPayment(orderId);

        if (response.isCompleted()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
