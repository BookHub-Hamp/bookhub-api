package com.hampcode.mapper;

import com.hampcode.dto.PurchaseCreateUpdateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseItemCreateUpdateDTO;
import com.hampcode.dto.PurchaseItemDTO;
import com.hampcode.model.entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    private final ModelMapper modelMapper;

    public PurchaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    //Convertir PurchaseCreateUpdateDTO a Purchase (Crear una compra)
    public Purchase toPurchaseEntity(PurchaseCreateUpdateDTO purchaseDTO) {
        Purchase purchase = modelMapper.map(purchaseDTO, Purchase.class);

        User user = new User();
        user.setId(purchaseDTO.getCustomerId());
        purchase.setUser(user);

        //Mapear manualmente los items de la compra
        purchase.setItems(purchaseDTO.getItems().stream()
                .map(this::toPurchaseItemEntity)
                .toList());

        return purchase;
    }

    //Convertir Purchase a PurchaseDTO (Mostrar una compra)
    public PurchaseDTO toPurchaseDTO(Purchase purchase) {
        PurchaseDTO purchaseDTO = modelMapper.map(purchase, PurchaseDTO.class);

        purchaseDTO.setItems(purchase.getItems().stream()
                .map(this::toPurchaseItemDTO)
                .toList());
        return purchaseDTO;
    }

    private PurchaseItem toPurchaseItemEntity(PurchaseItemCreateUpdateDTO itemDTO) {
        PurchaseItem item = modelMapper.map(itemDTO, PurchaseItem.class);
        Book book = new Book();
        book.setId(itemDTO.getBookId());
        item.setBook(book);
        return item;
    }

    private PurchaseItemDTO toPurchaseItemDTO(PurchaseItem item) {
        PurchaseItemDTO itemDTO = modelMapper.map(item, PurchaseItemDTO.class);
        itemDTO.setBookTitle(item.getBook().getTitle());
        return itemDTO;
    }

}
