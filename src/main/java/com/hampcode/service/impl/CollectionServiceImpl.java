package com.hampcode.service.impl;

import com.hampcode.dto.CollectionCreateUpdateDTO;
import com.hampcode.dto.CollectionDetailsDTO;
import com.hampcode.mapper.CollectionMapper;
import com.hampcode.model.entity.Collection;
import com.hampcode.model.entity.Customer;
import com.hampcode.repository.CollectionRepository;
import com.hampcode.repository.CustomerRepository;
import com.hampcode.repository.UserRepository;
import com.hampcode.service.CollectionService;
import com.hampcode.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CustomerRepository customerRepository;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional
    public CollectionDetailsDTO createCollection(CollectionCreateUpdateDTO collectionDTO) {
        // Verificar si el cliente existe
        Customer customer = customerRepository.findById(collectionDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + collectionDTO.getCustomerId()));

        // Convertir el DTO en una entidad Collection
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection.setCustomer(customer);  // Asignar el cliente
        collection.setCreatedAt(LocalDateTime.now());

        // Guardar la colección y mapear el resultado a DTO
        Collection savedCollection = collectionRepository.save(collection);
        return collectionMapper.toDetailsDto(savedCollection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionDetailsDTO> getCollectionsByUser(Integer userId) {
        // Obtener todas las colecciones del usuario
        List<Collection> collections = collectionRepository.findByCustomerId(userId);
        return collections.stream()
                .map(collectionMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionDetailsDTO getCollectionById(Integer collectionId) {
        // Obtener la colección por su ID
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + collectionId));
        return collectionMapper.toDetailsDto(collection);
    }

    @Override
    @Transactional
    public CollectionDetailsDTO updateCollection(Integer collectionId, CollectionCreateUpdateDTO collectionDTO) {
        // Buscar la colección existente
        Collection existingCollection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + collectionId));

        // Actualizar los campos de la colección
        existingCollection.setName(collectionDTO.getName());
        existingCollection.setUpdatedAt(LocalDateTime.now());

        // Guardar y retornar el DTO actualizado
        Collection updatedCollection = collectionRepository.save(existingCollection);
        return collectionMapper.toDetailsDto(updatedCollection);
    }

    @Override
    @Transactional
    public void deleteCollection(Integer collectionId) {
        // Verificar si la colección existe
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + collectionId));

        // Eliminar la colección
        collectionRepository.delete(collection);
    }
}
