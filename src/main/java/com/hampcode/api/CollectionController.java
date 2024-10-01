package com.hampcode.api;

import com.hampcode.dto.CollectionCreateUpdateDTO;
import com.hampcode.dto.CollectionDetailsDTO;
import com.hampcode.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")  // Permitir solo a CUSTOMER y ADMIN
public class CollectionController {

    private final CollectionService collectionService;

    // Crear una nueva colección
    @PostMapping
    public ResponseEntity<CollectionDetailsDTO> createCollection(@RequestBody CollectionCreateUpdateDTO collectionDTO) {
        CollectionDetailsDTO savedCollection = collectionService.createCollection(collectionDTO);
        return new ResponseEntity<>(savedCollection, HttpStatus.CREATED);
    }

    // Obtener colecciones por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollectionDetailsDTO>> getCollectionsByUser(@PathVariable Integer userId) {
        List<CollectionDetailsDTO> collections = collectionService.getCollectionsByUser(userId);
        return ResponseEntity.ok(collections);
    }

    // Obtener una colección por ID
    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailsDTO> getCollectionById(@PathVariable Integer collectionId) {
        CollectionDetailsDTO collection = collectionService.getCollectionById(collectionId);
        return ResponseEntity.ok(collection);
    }

    // Actualizar una colección existente
    @PutMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailsDTO> updateCollection(@PathVariable Integer collectionId, @RequestBody CollectionCreateUpdateDTO collectionDTO) {
        CollectionDetailsDTO updatedCollection = collectionService.updateCollection(collectionId, collectionDTO);
        return ResponseEntity.ok(updatedCollection);
    }

    // Eliminar una colección
    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Integer collectionId) {
        collectionService.deleteCollection(collectionId);
        return ResponseEntity.noContent().build();
    }
}
