package com.hampcode.mapper;

import com.hampcode.dto.PurchaseCreateDTO;
import com.hampcode.dto.PurchaseDTO;
import com.hampcode.dto.PurchaseItemCreateDTO;
import com.hampcode.dto.PurchaseItemDTO;
import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.Purchase;
import com.hampcode.model.entity.PurchaseItem;
import com.hampcode.model.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    private final ModelMapper modelMapper;

    public PurchaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        //Configuración de la estrategia de mapeo
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    //Convertir PurchaseCreateDTO a Purchase (Crear una compra)
    public Purchase toPurchaseCreateDTO(PurchaseCreateDTO purchaseDTO){
        Purchase purchase= modelMapper.map(purchaseDTO, Purchase.class);

        //Mapear manualmente el cliente
        User customer = new User();
        customer.setId(purchaseDTO.getCustomerId());
        purchase.setCustomer(customer);

        //Mapear manualmente los items de la compra
        purchase.setItems(purchaseDTO.getItems().stream()
                .map(this::toPurchaseItemEntity)
                .toList());

        return purchase;
    }

    //Convertir Purchase a PurchaseDTO (Mostrar una compra)
    public PurchaseDTO toPurchaseDTO(Purchase purchase){
        PurchaseDTO purchaseDTO = modelMapper.map(purchase, PurchaseDTO.class);

        //Mapear manualmente el nombre del cliente
        purchaseDTO.setCustomerName(purchase.getCustomer().getFirstName()+" "+purchase.getCustomer().getLastName());

        //Mapear manualmente los items de la compra
        purchaseDTO.setItems(purchase.getItems().stream()
                .map(this::toPurchaseItemDTO)
                .toList());
        return purchaseDTO;
    }

    //Convertir PurchaseItemCreateDTO a PurchaseItem
    private PurchaseItem toPurchaseItemEntity(PurchaseItemCreateDTO itemDTO) {
        PurchaseItem item = modelMapper.map(itemDTO, PurchaseItem.class);
        Book book = new Book();
        book.setId(itemDTO.getBookId());
        item.setBook(book);
        return item;
    }

    //Convertir PurchaseItem a PurchaseItemDTO
    private PurchaseItemDTO toPurchaseItemDTO(PurchaseItem item){
        PurchaseItemDTO itemDTO= modelMapper.map(item, PurchaseItemDTO.class);
        itemDTO.setBookTitle(item.getBook().getTitle());
        return itemDTO;
    }

}
