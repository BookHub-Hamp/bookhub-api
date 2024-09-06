package com.hampcode.repository;

import com.hampcode.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
}