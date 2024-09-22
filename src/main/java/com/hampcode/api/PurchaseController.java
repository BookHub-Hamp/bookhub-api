package com.hampcode.api;

import com.hampcode.dto.PurchaseCreateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseReportDTO;
import com.hampcode.model.entity.Purchase;
import com.hampcode.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')") // Aplicar restricción a nivel de clase
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<Purchase>> listAllPurchases() {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }


    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseCreateDTO purchaseCreateDTO) {
        PurchaseDTO savedPurchase = purchaseService.createPurchase(purchaseCreateDTO);
        return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
    }

    //@GetMapping("/user/{userId}")
    //public ResponseEntity<List<PurchaseDTO>> getPurchaseHistory(@PathVariable Integer userId) {
    @GetMapping("/user")
    public ResponseEntity<List<PurchaseDTO>> getPurchaseHistory() {
        List<PurchaseDTO> purchaseHistory = purchaseService.getPurchaseHistoryByUserId();
        return ResponseEntity.ok(purchaseHistory);
    }

    @GetMapping("/report")
    public ResponseEntity<List<PurchaseReportDTO>> getPurchaseReport(){
        List<PurchaseReportDTO> reports = purchaseService.getPurchaseReportByDate();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Integer id) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(purchase);
    }

    // Endpoint para confirmar la compra (calcular total)
    @PutMapping("/confirm/{id}")
    public ResponseEntity<Purchase> confirmPurchase(@PathVariable Integer id) {
        Purchase confirmedPurchase = purchaseService.confirmPurchase(id);
        return ResponseEntity.ok(confirmedPurchase);
    }
}
