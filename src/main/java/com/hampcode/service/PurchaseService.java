package com.hampcode.service;

import com.hampcode.dto.PurchaseCreateUpdateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseReportDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO createPurchase(PurchaseCreateUpdateDTO purchaseDTO);
    //List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId);
    List<PurchaseDTO> getPurchaseHistoryByUserId();

    List<PurchaseReportDTO> getPurchaseReportByDate();

    List<PurchaseDTO> getAllPurchases();
    PurchaseDTO confirmPurchase(Integer purchaseId);
    PurchaseDTO getPurchaseById(Integer id);


}
