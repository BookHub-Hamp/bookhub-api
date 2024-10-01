package com.hampcode.mapper;

import com.hampcode.dto.CollectionCreateUpdateDTO;
import com.hampcode.dto.CollectionDetailsDTO;
import com.hampcode.model.entity.Collection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {

    private final ModelMapper modelMapper;

    public CollectionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Configurar ModelMapper para usar estrategia estricta
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // Mapeo de Collection a CollectionDetailsDTO (para mostrar información completa)
    public CollectionDetailsDTO toDetailsDto(Collection collection) {
        CollectionDetailsDTO collectionDetailsDTO = modelMapper.map(collection, CollectionDetailsDTO.class);

        // Mapear manualmente el nombre completo del cliente
        collectionDetailsDTO.setCustomerName(
                collection.getCustomer().getFirstName() + " " + collection.getCustomer().getLastName()
        );

        return collectionDetailsDTO;
    }

    // Mapeo de CollectionCreateUpdateDTO a Collection (para crear/actualizar)
    public Collection toEntity(CollectionCreateUpdateDTO collectionCreateUpdateDTO) {
        return modelMapper.map(collectionCreateUpdateDTO, Collection.class);
    }

    // Mapeo de Collection a CollectionCreateUpdateDTO (para casos donde necesites regresar el DTO de creación/actualización)
    public CollectionCreateUpdateDTO toCreateUpdateDto(Collection collection) {
        return modelMapper.map(collection, CollectionCreateUpdateDTO.class);
    }
}
