package com.hampcode.service.impl;

import com.hampcode.dto.PurchaseCreateUpdateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseReportDTO;
import com.hampcode.exception.ResourceNotFoundException;
import com.hampcode.mapper.PurchaseMapper;
import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.Purchase;
import com.hampcode.model.entity.Customer;
import com.hampcode.model.entity.User;
import com.hampcode.model.enums.PaymentStatus;
import com.hampcode.repository.BookRepository;
import com.hampcode.repository.PurchaseRepository;
import com.hampcode.repository.UserRepository;
import com.hampcode.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public PurchaseDTO createPurchase(PurchaseCreateUpdateDTO purchaseDTO) {
        // Convertir el DTO en una entidad Purchase
        Purchase purchase = purchaseMapper.toPurchaseEntity(purchaseDTO);

        // Verificar si el cliente existe en la base de datos
        /*User user = userRepository.findById(purchaseDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + purchaseDTO.getCustomerId()));*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(ResourceNotFoundException::new);
        }


        purchase.setUser(user); // Asociar el cliente a la compra

        // Verificar si los libros existen en la base de datos antes de proceder
        purchase.getItems().forEach(item -> {
            Book book = bookRepository.findById(item.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + item.getBook().getId()));
            item.setBook(book); // Asociar el libro existente al PurchaseItem
            item.setPurchase(purchase); // Asociar el PurchaseItem a la compra actual
        });

        // Establecer la fecha de creación y estado de pago
        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setPaymentStatus(PaymentStatus.PENDING);

        // Calcular el total basado en la cantidad de libros comprados
        Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        purchase.setTotal(total);

        // Guardar la compra
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Retornar el DTO mapeado
        return purchaseMapper.toPurchaseDTO(savedPurchase);
    }


  /* @Override
    @Transactional(readOnly = true)
    public List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId) {
        return purchaseRepository.findByUserId(userId)
                .stream()
                .map(purchaseMapper::toPurchaseDTO)
                .collect(Collectors.toList());
    }*/


    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDTO> getPurchaseHistoryByUserId() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(ResourceNotFoundException::new);
        }

        return purchaseRepository.findByUserId(user.getId()).stream()
                .map(purchaseMapper::toPurchaseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseReportDTO> getPurchaseReportByDate() {
        List<Object[]> results = purchaseRepository.getPurchaseReportByDate();

        // Mapea cada Object[] a un PurchaseReportDTO
        return results.stream().map(result ->
                new PurchaseReportDTO(
                        ((Integer) result[0]).intValue(),  // Cast de la cantidad
                        (String) result[1]                // Cast de la fecha
                )
        ).collect(Collectors.toList());
    }

    /////

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchaseMapper::toPurchaseDTO)
                .collect(Collectors.toList());
    }

    @Override
     public PurchaseDTO getPurchaseById(Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));
        return purchaseMapper.toPurchaseDTO(purchase);  // Retornar el DTO en lugar de la entidad
    }


    @Override
    @Transactional
    public PurchaseDTO confirmPurchase(Integer purchaseId) {
        // Obtener la entidad Purchase directamente desde el repositorio
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        // Confirmar la compra y calcular el total
       /* Float total = purchase.getItems()
                .stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        purchase.setTotal(total);*/
        purchase.setPaymentStatus(PaymentStatus.PAID);

        // Guardar y retornar el DTO actualizado
        Purchase updatedPurchase = purchaseRepository.save(purchase);
        return purchaseMapper.toPurchaseDTO(updatedPurchase);
    }

}
