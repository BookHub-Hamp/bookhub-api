package com.hampcode.service.impl;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;
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
import com.hampcode.security.UserPrincipal;
import com.hampcode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AuthorRepository authorRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

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

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        //Autenticar al usuario utilizando AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        //Una vez autenticado, el objeto authentication contiene la información del usuario autenticado
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        // Verificar si es un administrador
        boolean isAdmin = user.getRole().getName().equals(ERole.ADMIN);

        String token=tokenProvider.createAccessToken(authentication);

        AuthResponseDTO responseDTO = userMapper.toAuthResponseDTO(user, token);

        return responseDTO;
    }


    @Transactional
    @Override
    public UserProfileDTO updateUserProfile(Integer id, UserProfileDTO userProfileDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //Verificar si ya existe un cliente o autor con el mismo nombre y apellido (excepto el usuario actual)
        boolean existsAsCustomer = customerRepository.existsByFirstNameAndLastNameAndUserIdNot(
                userProfileDTO.getFirstName(), userProfileDTO.getLastName(), id);
        boolean  existsAsAuthor = authorRepository.existsByFirstNameAndLastNameAndUserIdNot(
                userProfileDTO.getFirstName(), userProfileDTO.getLastName(), id);
        System.out.println("Author exists: " + existsAsAuthor);

        if(existsAsCustomer || existsAsAuthor){
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }



        if(user.getCustomer()!=null){

            user.getCustomer().setFirstName(userProfileDTO.getFirstName());
            user.getCustomer().setLastName(userProfileDTO.getLastName());
            user.getCustomer().setShippingAddress(userProfileDTO.getShippingAddress());
        }

        if(user.getAuthor()!=null){
            user.getAuthor().setFirstName(userProfileDTO.getFirstName());
            user.getAuthor().setLastName(userProfileDTO.getLastName());
            user.getAuthor().setBio(userProfileDTO.getBio());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toUserProfileDTO(updatedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDTO getUserProfileById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return userMapper.toUserProfileDTO(user);
    }

    private UserProfileDTO registerUserWithRole(UserRegistrationDTO registrationDTO, ERole roleEnum) {

        //Verificar si el email ya esta registrado o si ya si existe un usuario con el mismo nombre y apellido
        boolean existsByEmail = userRepository.existsByEmail(registrationDTO.getEmail());
        boolean existsAsCustomer = customerRepository.existsByFirstNameAndLastName(registrationDTO.getFirstName(), registrationDTO.getLastName());
        boolean existsAsAuthor = authorRepository.existsByFirstNameAndLastName(registrationDTO.getFirstName(), registrationDTO.getLastName());

        if(existsByEmail){
            throw new IllegalArgumentException("El email ya esta registrado");
        }

        if(existsAsCustomer || existsAsAuthor){
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));

        registrationDTO.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        User user = userMapper.toUserEntity(registrationDTO);
        user.setRole(role);

        if(roleEnum == ERole.CUSTOMER){
            Customer customer = new Customer();
            customer.setFirstName(registrationDTO.getFirstName());
            customer.setLastName(registrationDTO.getLastName());
            customer.setShippingAddress(registrationDTO.getShippingAddress());
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUser(user);
            user.setCustomer(customer);
        }else if(roleEnum == ERole.AUTHOR){
                Author author = new Author();
                author.setFirstName(registrationDTO.getFirstName());
                author.setLastName(registrationDTO.getLastName());
                author.setBio(registrationDTO.getBio());
                author.setCreatedAt(LocalDateTime.now());
                author.setUser(user);
                user.setAuthor(author);
         }

        User savedUser = userRepository.save(user);

        return userMapper.toUserProfileDTO(savedUser);
    }
}
