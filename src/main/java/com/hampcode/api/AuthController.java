package com.hampcode.api;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserRegistrationDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Endpoint para registrar clientes
    @PostMapping("/register/customer")
    public ResponseEntity<UserProfileDTO> registerCustomer(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserProfileDTO userProfile = userService.registerCustomer(userRegistrationDTO);
        return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
    }

    // Endpoint para registrar autores
    @PostMapping("/register/author")
    public ResponseEntity<UserProfileDTO> registerAuthor(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserProfileDTO userProfile = userService.registerAuthor(userRegistrationDTO);
        return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
    }

    // Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        AuthResponseDTO authResponse = userService.login(loginDTO);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    // Login específico para Administradores
    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDTO> adminLogin(@RequestBody LoginDTO loginDTO) {
        AuthResponseDTO authResponse = userService.adminLogin(loginDTO);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
