package com.hampcode.service;

import com.hampcode.model.entity.Purchase;
import java.util.List;
public interface PurchaseService {
    Purchase createPurchase(Purchase purchase);  // Crear la compra
    Purchase confirmPurchase(Integer purchaseId); // Confirmar la compra
    List<Purchase> getAllPurchases();
    Purchase getPurchaseById(Integer id);
}
