package com.hampcode.service;

import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;

public interface UserService {

    // Registrar un cliente
    UserProfileDTO registerCustomer(UserRegistrationDTO registrationDTO);

    // Registrar un autor
    UserProfileDTO registerAuthor(UserRegistrationDTO registrationDTO);

    // Actualizar el perfil de usuario
    UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO);

    // Obtener el perfil de usuario por ID
    UserProfileDTO getUserProfileById(Integer id);
}
