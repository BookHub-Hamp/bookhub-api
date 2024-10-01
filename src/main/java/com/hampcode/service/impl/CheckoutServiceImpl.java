package com.hampcode.service.impl;


import com.hampcode.dto.PurchaseDTO;
import com.hampcode.paypal.dto.OrderCaptureResponse;
import com.hampcode.paypal.dto.OrderResponse;
import com.hampcode.paypal.dto.PaypalCaptureResponse;
import com.hampcode.paypal.dto.PaypalOrderResponse;
import com.hampcode.paypal.service.PayPalService;
import com.hampcode.service.CheckoutService;
import com.hampcode.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final PayPalService paypalService;
    private final PurchaseService purchaseService;

    @Override
    public PaypalOrderResponse createPaypalPaymentUrl(Integer purchaseId, String returnUrl, String cancelUrl) {
        OrderResponse orderResponse = paypalService.createOrder(purchaseId, returnUrl, cancelUrl);

        String paypalUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();

        return new PaypalOrderResponse(paypalUrl);
    }

    @Override
    public PaypalCaptureResponse capturePaypalPayment(String orderId) {
        OrderCaptureResponse orderCaptureResponse = paypalService.captureOrder(orderId);
        boolean completed = orderCaptureResponse.getStatus().equals("COMPLETED");

        PaypalCaptureResponse paypalCaptureResponse = new PaypalCaptureResponse();
        paypalCaptureResponse.setCompleted(completed);

        if (completed) {
            String purchaseIdStr = orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId();
            PurchaseDTO purchaseDTO = purchaseService.confirmPurchase(Integer.parseInt(purchaseIdStr));
            paypalCaptureResponse.setPurchaseId(purchaseDTO.getId());
        }

        return paypalCaptureResponse;
    }
}
