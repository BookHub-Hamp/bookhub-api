package com.hampcode.service.impl;

import com.hampcode.model.entity.Purchase;
import com.hampcode.model.entity.PurchaseItem;
import com.hampcode.model.enums.PaymentStatus;
import com.hampcode.repository.PurchaseRepository;
import com.hampcode.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Override
    @Transactional
    public Purchase createPurchase(Purchase purchase) {
        // Establecer la fecha de creación
        purchase.setCreatedAt(LocalDateTime.now());

        // Asignar el estado de pago en PENDING
        purchase.setPaymentStatus(PaymentStatus.PENDING);

        // Asignar la compra a cada PurchaseItem usando lambda y streams
        purchase.getItems().forEach(item -> item.setPurchase(purchase));

        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public Purchase getPurchaseById(Integer id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseHistoryByUserId(Integer userId) {
        return purchaseRepository.findByCustomerId(userId);
    }

    @Override
    @Transactional
    public Purchase confirmPurchase(Integer purchaseId) {
        Purchase purchase = getPurchaseById(purchaseId);

        // Confirmar la compra y calcular el total usando Stream API y lambda
        Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getDownloadsAvailable())
                .reduce(0f, Float::sum);

        purchase.setTotal(total);

        // Actualizar el estado de pago a confirmado (se podría cambiar a PAID en el futuro)
        purchase.setPaymentStatus(PaymentStatus.PENDING); // Dejar en PENDING hasta confirmar con PayPal

        return purchaseRepository.save(purchase);
    }
}
