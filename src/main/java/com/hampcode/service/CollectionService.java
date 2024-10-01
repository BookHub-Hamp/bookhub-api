package com.hampcode.service;

import com.hampcode.dto.CollectionCreateUpdateDTO;
import com.hampcode.dto.CollectionDetailsDTO;

import java.util.List;

public interface CollectionService {
    CollectionDetailsDTO createCollection(CollectionCreateUpdateDTO collectionDTO);
    List<CollectionDetailsDTO> getCollectionsByUser(Integer userId);
    CollectionDetailsDTO getCollectionById(Integer collectionId);
    CollectionDetailsDTO updateCollection(Integer collectionId, CollectionCreateUpdateDTO collectionDTO);
    void deleteCollection(Integer collectionId);
}
