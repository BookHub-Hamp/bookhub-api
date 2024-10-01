package com.hampcode.service;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;

public interface UserService {

        // Register a customer
        UserProfileDTO registerCustomer(UserRegistrationDTO registrationDTO);

        // Register an author
        UserProfileDTO registerAuthor(UserRegistrationDTO registrationDTO);

        // Authenticate user (login)
        AuthResponseDTO login(LoginDTO loginDTO);

        // Actualizar el perfil de usuario
        UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO);

        // Get user profile by ID
        UserProfileDTO getUserProfileById(Integer id);
}
