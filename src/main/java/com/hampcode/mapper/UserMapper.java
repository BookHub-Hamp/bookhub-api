package com.hampcode.mapper;

import com.hampcode.dto.AuthResponseDTO;
import com.hampcode.dto.LoginDTO;
import com.hampcode.dto.UserProfileDTO;
import com.hampcode.dto.UserRegistrationDTO;
import com.hampcode.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toUserEntity(UserRegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }

    public UserProfileDTO toUserProfileDTO(User user) {
        UserProfileDTO userProfileDTO =  modelMapper.map(user, UserProfileDTO.class);

        if(user.getCustomer()!=null){
            userProfileDTO.setFirstName(user.getCustomer().getFirstName());
            userProfileDTO.setLastName(user.getCustomer().getLastName());
            userProfileDTO.setShippingAddress(user.getCustomer().getShippingAddress());
        }

        if(user.getAuthor()!=null){
            userProfileDTO.setFirstName(user.getAuthor().getFirstName());
            userProfileDTO.setLastName(user.getAuthor().getLastName());
            userProfileDTO.setBio(user.getAuthor().getBio());
        }

        return userProfileDTO;
    }

    //Convertir de LoginDTO a User (cuando procesas el login)
    public User toUserEntity(LoginDTO loginDTO) {
        return modelMapper.map(loginDTO, User.class);
    }

    //Convertir de User a AuthResponseDTO para la respuesta de autenticación
    public AuthResponseDTO toAuthResponseDTO(User user, String token){
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token);

        // Obtener el nombre y apellido
        String firstName = (user.getCustomer() != null) ? user.getCustomer().getFirstName()
                : (user.getAuthor() != null) ? user.getAuthor().getFirstName()
                : "Admin";
        String lastName = (user.getCustomer() != null) ? user.getCustomer().getLastName()
                : (user.getAuthor() != null) ? user.getAuthor().getLastName()
                : "User";

        authResponseDTO.setFirstName(firstName);
        authResponseDTO.setLastName(lastName);

        authResponseDTO.setRole(user.getRole().getName().name());

        return authResponseDTO;
    }


}
