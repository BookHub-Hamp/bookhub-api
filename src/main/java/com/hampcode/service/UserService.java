package com.hampcode.service;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;

public interface UserService {

    // Registrar un cliente
    UserProfileDTO registerCustomer(UserRegistrationDTO registrationDTO);

    // Registrar un autor
    UserProfileDTO registerAuthor(UserRegistrationDTO registrationDTO);

    // Autenticar usuario (login)
    AuthResponseDTO login(LoginDTO loginDTO);

    AuthResponseDTO adminLogin(LoginDTO loginDTO);

    // Actualizar el perfil de usuario
    UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO);

    // Obtener el perfil de usuario por ID
    UserProfileDTO getUserProfileById(Integer id);
}
