package com.hampcode.api;

import com.hampcode.model.entity.Collection;
import com.hampcode.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<Collection> createCollection(@RequestBody Collection collection) {
        Collection savedCollection = collectionService.createCollection(collection);
        return new ResponseEntity<>(savedCollection, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Collection>> getCollectionsByUser(@PathVariable Integer userId) {
        List<Collection> collections = collectionService.getCollectionsByUser(userId);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<Collection> getCollectionById(@PathVariable Integer collectionId) {
        Collection collection = collectionService.getCollectionById(collectionId);
        return ResponseEntity.ok(collection);
    }

    @PutMapping("/{collectionId}")
    public ResponseEntity<Collection> updateCollection(@PathVariable Integer collectionId, @RequestBody Collection collection) {
        Collection updatedCollection = collectionService.updateCollection(collectionId, collection);
        return ResponseEntity.ok(updatedCollection);
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Integer collectionId) {
        collectionService.deleteCollection(collectionId);
        return ResponseEntity.noContent().build();
    }
}
