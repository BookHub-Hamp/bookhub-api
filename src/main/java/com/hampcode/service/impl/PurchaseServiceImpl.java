package com.hampcode.service.impl;

import com.hampcode.dto.PurchaseCreateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseReportDTO;
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
    public PurchaseDTO createPurchase(PurchaseCreateDTO purchaseCreateDTO) {
        //Convertir PurchaseCreateDTO a Purchase
        Purchase purchase = purchaseMapper.toPurchaseCreateDTO(purchaseCreateDTO);

        /*User customer = userRepository.findById(purchaseCreateDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));*/

        // Buscar el usuario (antes 'customer')
       /* User user = userRepository.findById(purchaseCreateDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));*/

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(ResourceNotFoundException::new);
        }

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
        //purchase.setCustomer(customer);
        purchase.setUser(user);  // Cambiado a 'user'
        purchase.setTotal(total);
        purchase.getItems().forEach(item -> item.setPurchase(purchase));




        Purchase savePurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseDTO(savePurchase);
    }

    @Override
    @Transactional(readOnly = true)
    //public List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId) {
    public List<PurchaseDTO> getPurchaseHistoryByUserId() {
        /*return purchaseRepository.findByCustomerId(userId).stream()
                .map(purchaseMapper::toPurchaseDTO)
                .toList();*/

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

    @Transactional(readOnly = true)
    @Override
    public List<PurchaseReportDTO> getPurchaseReportByDate() {
        List<Object[]> results = purchaseRepository.getPurchaseReportByDate();
        //Mapeo de la lista de objetos a una lista de PurchaseReportDTO
        List<PurchaseReportDTO> purchaseReportDTOS = results.stream()
                .map(result ->
                        new PurchaseReportDTO (
                                ((Integer)result[0]).intValue(),
                                (String)result[1]
                        )
                ).toList();
        return purchaseReportDTOS;
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
