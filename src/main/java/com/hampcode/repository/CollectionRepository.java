package com.hampcode.repository;

import com.hampcode.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    // Método para encontrar las colecciones de un usuario específico
    List<Collection> findByCustomerId(Integer customerId);
}