package com.hampcode.service.impl;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;
import com.hampcode.exception.BadRequestException;
import com.hampcode.exception.InvalidCredentialsException;
import com.hampcode.exception.ResourceNotFoundException;
import com.hampcode.exception.RoleNotFoundException;
import com.hampcode.mapper.UserMapper;
import com.hampcode.model.entity.Author;
import com.hampcode.model.entity.Customer;
import com.hampcode.model.entity.Role;
import com.hampcode.model.entity.User;
import com.hampcode.model.enums.ERole;
import com.hampcode.repository.AuthorRepository;
import com.hampcode.repository.CustomerRepository;
import com.hampcode.repository.RoleRepository;
import com.hampcode.repository.UserRepository;
import com.hampcode.security.TokenProvider;
import com.hampcode.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AuthorRepository authorRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager; // Necesario para la autenticación
    private final TokenProvider tokenProvider; // Necesario para la creación de tokens JWT

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

    @Transactional
    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // Buscar el usuario por email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + loginDTO.getEmail()));

        // Verificar si la contraseña es correcta
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }

        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        // Generar el token JWT usando el TokenProvider
        String token = tokenProvider.createAccessToken(authentication);

        // Generar la respuesta de autenticación, con el rol correspondiente
        AuthResponseDTO response = userMapper.toAuthResponseDTO(user, token);

        // Retornar la respuesta
        return response;
    }


    // Método genérico para registrar un usuario con un rol específico
    private UserProfileDTO registerUserWithRole(UserRegistrationDTO registrationDTO, ERole roleEnum) {

        // Verificar si el email ya está registrado o si ya existe un usuario con el mismo nombre y apellido
        boolean emailExists = userRepository.existsByEmail(registrationDTO.getEmail());
        boolean existsAsCustomer = customerRepository.existsByFirstNameAndLastName(registrationDTO.getFirstName(), registrationDTO.getLastName());
        boolean existsAsAuthor = authorRepository.existsByFirstNameAndLastName(registrationDTO.getFirstName(), registrationDTO.getLastName());

        if (emailExists) {
            throw new UsernameNotFoundException("El email ya está registrado");
        }

        if (existsAsCustomer || existsAsAuthor) {
            throw new BadRequestException("Ya existe un usuario con el mismo nombre y apellido");
        }


        // Asignar el rol del usuario
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado"));

        // Cifrar la contraseña
        registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Convertir el DTO a una entidad User
        User user = userMapper.toUserEntity(registrationDTO);
        user.setRole(role); // Asignar el rol al usuario

        // Asignar la entidad específica basada en el rol
        if (roleEnum == ERole.CUSTOMER) {
            // Crear un cliente
            Customer customer = new Customer();
            customer.setFirstName(registrationDTO.getFirstName());
            customer.setLastName(registrationDTO.getLastName());
            customer.setShippingAddress(registrationDTO.getShippingAddress());
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUser(user);  // Enlazar el cliente con el usuario
            user.setCustomer(customer);
        } else if (roleEnum == ERole.AUTHOR) {
            // Crear un autor
            Author author = new Author();
            author.setFirstName(registrationDTO.getFirstName());
            author.setLastName(registrationDTO.getLastName());
            author.setBio(registrationDTO.getBio());
            author.setCreatedAt(LocalDateTime.now());
            author.setUser(user);  // Enlazar el autor con el usuario
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
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Verificar si ya existe un cliente o autor con el mismo nombre y apellido (excepto el usuario actual)
        // La verificación se realiza excluyendo el usuario actual para permitir que actualice su propio perfil
        // sin que se genere un conflicto de duplicidad en los nombres y apellidos.
        boolean existsAsCustomer = customerRepository.existsByFirstNameAndLastNameAndUserIdNot(userProfileDTO.getFirstName(), userProfileDTO.getLastName(), id);
        boolean existsAsAuthor = authorRepository.existsByFirstNameAndLastNameAndUserIdNot(userProfileDTO.getFirstName(), userProfileDTO.getLastName(), id);

        if (existsAsCustomer || existsAsAuthor) {
            throw new BadRequestException("Ya existe un usuario con el mismo nombre y apellido");
        }


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

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Convertir a UserProfileDTO para la respuesta
        return userMapper.toUserProfileDTO(user);
    }
}
