package com.hampcode.service.impl;

import com.hampcode.dto.PurchaseCreateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.exception.ResourceNotFoundException;
import com.hampcode.mapper.PurchaseMapper;
import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.Purchase;
import com.hampcode.model.entity.PurchaseItem;
import com.hampcode.model.entity.User;
import com.hampcode.model.enums.PaymentStatus;
import com.hampcode.repository.BookRepository;
import com.hampcode.repository.PurchaseRepository;
import com.hampcode.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PurchaseMapper purchaseMapper;


    @Override
    @Transactional
    public PurchaseDTO createPurchase(PurchaseCreateDTO purchaseCreateDTO) {
        //Convertir PurchaseCreateDTO a Purchase
        Purchase purchase = purchaseMapper.toPurchaseCreateDTO(purchaseCreateDTO);

        User customer = userRepository.findById(purchaseCreateDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        purchase.getItems().forEach(item->{
            Book book = bookRepository.findById(item.getBook().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
            item.setBook(book);//Asignar el libro al item
            item.setPurchase(purchase);//Asignar la compra al item
        });

        Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setPaymentStatus(PaymentStatus.PENDING);
        purchase.setCustomer(customer);
        purchase.setTotal(total);
        purchase.getItems().forEach(item -> item.setPurchase(purchase));




        Purchase savePurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseDTO(savePurchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId) {
        return purchaseRepository.findByCustomerId(userId).stream()
                .map(purchaseMapper::toPurchaseDTO)
                .toList();
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
    @Transactional
    public Purchase confirmPurchase(Integer purchaseId) {
        Purchase purchase = getPurchaseById(purchaseId);

        // Confirmar la compra y calcular el total usando Stream API y lambda
        Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        purchase.setTotal(total);

        // Actualizar el estado de pago a confirmado (se podría cambiar a PAID en el futuro)
        purchase.setPaymentStatus(PaymentStatus.PENDING); // Dejar en PENDING hasta confirmar con PayPal

        return purchaseRepository.save(purchase);
    }
}
