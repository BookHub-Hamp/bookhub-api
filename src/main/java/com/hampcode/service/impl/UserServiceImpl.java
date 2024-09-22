package com.hampcode.service.impl;

import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;
import com.hampcode.mapper.UserMapper;
import com.hampcode.model.entity.Author;
import com.hampcode.model.entity.Customer;
import com.hampcode.model.entity.Role;
import com.hampcode.model.entity.User;
import com.hampcode.model.enums.ERole;
import com.hampcode.repository.RoleRepository;
import com.hampcode.repository.UserRepository;
import com.hampcode.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserProfileDTO registerCustomer(UserRegistrationDTO registrationDTO) {
        return registerUserWithRole(registrationDTO, ERole.CUSTOMER);
    }

    @Transactional
    @Override
    public UserProfileDTO registerAuthor(UserRegistrationDTO registrationDTO) {
        return registerUserWithRole(registrationDTO, ERole.AUTHOR);
    }

    // Método genérico para registrar un usuario con un rol específico
    private UserProfileDTO registerUserWithRole(UserRegistrationDTO registrationDTO, ERole roleEnum) {
        // Verificar si el email ya está registrado
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Asignar el rol del usuario
       Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Cifrar la contraseña
        registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Convertir el DTO a una entidad User
        User user = userMapper.toUserEntity(registrationDTO);
        user.setRole(role); // Asignar el rol al usuario
        //user.setCreatedAt(LocalDateTime.now());

        // Asignar la entidad específica basada en el rol
        if (roleEnum == ERole.CUSTOMER) {
            Customer customer = new Customer();
            customer.setFirstName(registrationDTO.getFirstName());
            customer.setLastName(registrationDTO.getLastName());
            customer.setShippingAddress(registrationDTO.getShippingAddress());
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUser(user);
            user.setCustomer(customer);
        } else if (roleEnum == ERole.AUTHOR) {
            Author author = new Author();
            author.setFirstName(registrationDTO.getFirstName());
            author.setLastName(registrationDTO.getLastName());
            author.setBio(registrationDTO.getBio());
            author.setCreatedAt(LocalDateTime.now());
            author.setUser(user);
            user.setAuthor(author);
        }

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);

        // Convertir el usuario registrado a UserProfileDTO para la respuesta
        return userMapper.toUserProfileDTO(savedUser);
    }

    @Transactional
    @Override
    public UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO) {
        // Buscar el usuario por su ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar los campos específicos del perfil
        if (user.getCustomer() != null) {
            user.getCustomer().setFirstName(userProfileDTO.getFirstName());
            user.getCustomer().setLastName(userProfileDTO.getLastName());
            user.getCustomer().setShippingAddress(userProfileDTO.getShippingAddress());
        }

        if (user.getAuthor() != null) {
            user.getAuthor().setFirstName(userProfileDTO.getFirstName());
            user.getAuthor().setLastName(userProfileDTO.getLastName());
            user.getAuthor().setBio(userProfileDTO.getBio());
        }

        // Guardar los cambios en la base de datos
        User updatedUser = userRepository.save(user);

        // Convertir el usuario actualizado a UserProfileDTO para la respuesta
        return userMapper.toUserProfileDTO(updatedUser);
    }


    @Transactional
    @Override
    public UserProfileDTO getUserProfileById(Integer id) {
        // Usar Optional para buscar el usuario por su ID
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convertir a UserProfileDTO para la respuesta
        return userMapper.toUserProfileDTO(user);
    }
}
