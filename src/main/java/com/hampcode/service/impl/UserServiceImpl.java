package com.hampcode.service.impl;

import com.hampcode.model.entity.User;
import com.hampcode.repository.UserRepository;
import com.hampcode.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Transactional
    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }



        // Establecer fechas de creación
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
