package com.hampcode.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;         // El token JWT
    private String firstName;     // El primer nombre del usuario
    private String lastName;      // El apellido del usuario
    private String role;          // El rol del usuario (e.g., ROLE_CUSTOMER, ROLE_AUTHOR)
}
