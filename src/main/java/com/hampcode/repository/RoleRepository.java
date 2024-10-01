package com.hampcode.repository;

import com.hampcode.model.entity.Role;
import com.hampcode.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    //Buscar un rol por su nombre (usando el enum)
    Optional<Role> findByName(ERole name);
}
