package com.hampcode.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

        @NotBlank(message = "El nombre es obligatorio")
        private String firstName;

        @NotBlank(message = "El apellido es obligatorio")
        private String lastName;

        @Email(message = "El correo electrónico no es válido")
        @NotBlank(message = "El correo electrónico es obligatorio")
        private String email;

        @NotNull(message = "La contraseña es obligatoria")
        @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
        private String password;

        private String shippingAddress;
        private String bio;
}
