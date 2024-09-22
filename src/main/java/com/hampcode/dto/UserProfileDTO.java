package com.hampcode.dto;

import com.hampcode.model.enums.ERole;
import lombok.Data;

@Data
public class UserProfileDTO {

    private Integer id;
    private String email;
    private ERole role;  // El rol puede ser CUSTOMER o AUTHOR

    // Campos específicos para Customer
    private String firstName;  // Nombre del cliente o autor
    private String lastName;   // Apellido del cliente o autor
    private String shippingAddress;  // Solo para el cliente (Customer)

    // Campos específicos para Author
    private String bio;  // Solo para el autor (Author)
}
