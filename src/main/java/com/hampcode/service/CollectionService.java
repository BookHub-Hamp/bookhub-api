package com.hampcode.service;

import com.hampcode.model.entity.Collection;

import java.util.List;

public interface CollectionService {
    Collection createCollection(Collection collection);
    List<Collection> getCollectionsByUser(Integer userId);
    Collection getCollectionById(Integer collectionId);
    Collection updateCollection(Integer collectionId, Collection collection);
    void deleteCollection(Integer collectionId);
}
