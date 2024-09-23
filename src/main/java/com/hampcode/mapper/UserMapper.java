package com.hampcode.mapper;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;
import com.hampcode.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Convertir de UserRegistrationDTO a User (solo mapeo directo)
    public User toUserEntity(UserRegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }

    // Convertir de User a UserProfileDTO para la respuesta
    public UserProfileDTO toUserProfileDTO(User user) {
        UserProfileDTO userProfileDTO = modelMapper.map(user, UserProfileDTO.class);

        // Si es cliente, asignar los datos de cliente
        if (user.getCustomer() != null) {
            userProfileDTO.setFirstName(user.getCustomer().getFirstName());
            userProfileDTO.setLastName(user.getCustomer().getLastName());
            userProfileDTO.setShippingAddress(user.getCustomer().getShippingAddress());
        }
        // Si es autor, asignar los datos de autor
        if (user.getAuthor() != null) {
            userProfileDTO.setFirstName(user.getAuthor().getFirstName());
            userProfileDTO.setLastName(user.getAuthor().getLastName());
            userProfileDTO.setBio(user.getAuthor().getBio());  // Asignar bio si es autor
        }

        return userProfileDTO;
    }

    // Convertir de LoginDTO a User (cuando procesas el login)
    public User toUserEntity(LoginDTO loginDTO) {
        return modelMapper.map(loginDTO, User.class);
    }

    // Convertir de User a AuthResponseDTO para la respuesta de autenticación
    public AuthResponseDTO toAuthResponseDTO(User user, String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token); // Asignar el token


        // Si es cliente, asignar los datos de cliente
        if (user.getCustomer() != null) {
            authResponseDTO.setFirstName(user.getCustomer().getFirstName());
            authResponseDTO.setLastName(user.getCustomer().getLastName());
        }
        // Si es autor, asignar los datos de autor
        else if (user.getAuthor() != null) {
            authResponseDTO.setFirstName(user.getAuthor().getFirstName());
            authResponseDTO.setLastName(user.getAuthor().getLastName());
        }
        // Para cualquier usuario que no sea cliente ni autor (ej. Admin)
        else {
            authResponseDTO.setFirstName("Admin");
            authResponseDTO.setLastName("User");
        }

        // Asignar el rol del usuario
        authResponseDTO.setRole(user.getRole().getName().toString());

        return authResponseDTO;
    }
}
