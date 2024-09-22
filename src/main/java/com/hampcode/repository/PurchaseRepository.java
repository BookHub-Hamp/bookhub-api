package com.hampcode.repository;

import com.hampcode.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    //List<Purchase> findByCustomerId(Integer customerId);
    List<Purchase> findByUserId(Integer userId);


    @Query(value = "SELECT * FROM fn_list_purchase_report() ", nativeQuery = true)
    List<Object[]> getPurchaseReportByDate();
}