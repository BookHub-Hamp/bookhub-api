package com.hampcode.service;

import com.hampcode.dto.PurchaseCreateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.model.entity.Purchase;
import java.util.List;
public interface PurchaseService {
    PurchaseDTO createPurchase(PurchaseCreateDTO purchaseCreateDTO); // Crear la compra
    List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId);


    List<Purchase> getAllPurchases();
    Purchase confirmPurchase(Integer purchaseId); // Confirmar la compra
    Purchase getPurchaseById(Integer id);

}
