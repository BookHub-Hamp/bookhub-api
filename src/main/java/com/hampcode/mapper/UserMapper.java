package com.hampcode.mapper;

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

    // Convertir de User a UserProfileDTO (si existe para mostrar el perfil del usuario)
  /*  public UserProfileDTO toUserProfileDTO(User user) {
        return modelMapper.map(user, UserProfileDTO.class);
    }*/
}
