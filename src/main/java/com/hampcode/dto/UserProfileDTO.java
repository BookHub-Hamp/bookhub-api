package com.hampcode.dto;

import com.hampcode.model.enums.ERole;
import lombok.Data;

@Data
public class UserProfileDTO {

    private Integer id;
    private String email;
    private ERole role; // El rol puede ser CUSTOMER o AUTHOR
    private String firstName;
    private String lastName;

    private String shippingAddress;
    private String bio;
}
