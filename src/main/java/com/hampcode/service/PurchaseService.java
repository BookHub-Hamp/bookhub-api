package com.hampcode.service;

import com.hampcode.model.entity.Purchase;
import java.util.List;
public interface PurchaseService {
    Purchase createPurchase(Purchase purchase); // Crear la compra
    List<Purchase> getPurchaseHistoryByUserId(Integer userId);

    List<Purchase> getAllPurchases();
    Purchase confirmPurchase(Integer purchaseId); // Confirmar la compra
    Purchase getPurchaseById(Integer id);

}
