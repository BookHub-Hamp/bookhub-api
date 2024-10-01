package com.hampcode.api;

import com.hampcode.dto.PurchaseCreateUpdateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseReportDTO;
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
    public ResponseEntity<List<PurchaseDTO>> listAllPurchases() {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseCreateUpdateDTO purchaseDTO) {
        PurchaseDTO savedPurchase = purchaseService.createPurchase(purchaseDTO);
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
    public ResponseEntity<List<PurchaseReportDTO>> getPurchaseReport() {
        List<PurchaseReportDTO> report = purchaseService.getPurchaseReportByDate();
        return ResponseEntity.ok(report);
    }

    /////////
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Integer id) {
        PurchaseDTO purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(purchase);
    }

    // Endpoint para confirmar la compra (calcular total)
    @PutMapping("/confirm/{id}")
    public ResponseEntity<PurchaseDTO> confirmPurchase(@PathVariable Integer id) {
        PurchaseDTO confirmedPurchase = purchaseService.confirmPurchase(id);
        return ResponseEntity.ok(confirmedPurchase);
    }
}
