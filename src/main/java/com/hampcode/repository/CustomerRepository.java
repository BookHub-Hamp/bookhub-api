package com.hampcode.repository;

import com.hampcode.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    // Método para verificar si ya existe un cliente con el mismo nombre y apellido, excepto el usuario actual
    boolean existsByFirstNameAndLastNameAndUserIdNot(String firstName, String lastName, Integer userId);
}
