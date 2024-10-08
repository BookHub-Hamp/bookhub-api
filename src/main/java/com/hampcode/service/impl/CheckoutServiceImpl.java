package com.hampcode.service.impl;

import com.hampcode.dto.PaymentCaptureResponse;
import com.hampcode.dto.PaymentOrderResponse;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.integration.notification.email.dto.Mail;
import com.hampcode.integration.notification.email.service.EmailService;
import com.hampcode.integration.payment.paypal.dto.OrderCaptureResponse;
import com.hampcode.integration.payment.paypal.dto.OrderResponse;
import com.hampcode.integration.payment.paypal.service.PayPalService;
import com.hampcode.service.CheckoutService;
import com.hampcode.service.PurchaseService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final PayPalService payPalService;
    private final PurchaseService purchaseService;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public PaymentOrderResponse createPayment(Integer purchaseId, String returnUrl, String cancelUrl) {
        OrderResponse orderResponse =payPalService.createOrder(purchaseId, returnUrl, cancelUrl);

        String paypalUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();

        return new PaymentOrderResponse(paypalUrl);
    }

    @Override
    public PaymentCaptureResponse capturePayment(String orderId) throws MessagingException{
        OrderCaptureResponse orderCaptureResponse = payPalService.captureOrder(orderId);
        boolean completed = orderCaptureResponse.getStatus().equals("COMPLETED");

        PaymentCaptureResponse paypalCaptureResponse = new PaymentCaptureResponse();
        paypalCaptureResponse.setCompleted(completed);

        if (completed) {
            String purchaseIdStr = orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId();
            PurchaseDTO purchaseDTO = purchaseService.confirmPurchase(Integer.parseInt(purchaseIdStr));
            paypalCaptureResponse.setPurchaseId(purchaseDTO.getId());

            sendPurchaseConfirmationEmail(purchaseDTO);

        }
        return paypalCaptureResponse;
    }

    private void sendPurchaseConfirmationEmail(PurchaseDTO purchaseDTO) throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();


        Map<String, Object> model = new HashMap<>();
        model.put("user", userEmail);
        model.put("total", purchaseDTO.getTotal());
        model.put("orderUrl", "http://localhost:4200/order/" + purchaseDTO.getId());


        Mail mail = emailService.createMail(
                userEmail,
                "Confirmación de Compra",
                model,
                mailFrom
        );
        emailService.sendEmail(mail,"email/purchase-confirmation-template");
    }
}
