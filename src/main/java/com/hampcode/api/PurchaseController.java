package com.hampcode.api;

import com.hampcode.model.entity.Purchase;
import com.hampcode.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<Purchase>> listAllPurchases() {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }


    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        Purchase savedPurchase = purchaseService.createPurchase(purchase);
        return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Purchase>> getPurchaseHistory(@PathVariable Integer userId) {
        List<Purchase> purchaseHistory = purchaseService.getPurchaseHistoryByUserId(userId);
        return ResponseEntity.ok(purchaseHistory);
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
