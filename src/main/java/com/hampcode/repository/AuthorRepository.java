package com.hampcode.repository;

import com.hampcode.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    // Método para verificar si ya existe un autor con el mismo nombre y apellido, excepto el usuario actual
    boolean existsByFirstNameAndLastNameAndUserIdNot(String firstName, String lastName, Integer userId);
}
