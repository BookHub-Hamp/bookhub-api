package com.hampcode.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollectionCreateUpdateDTO {

    private Integer id;

    @NotBlank(message = "El nombre de la colección es obligatorio")
    @Size(max = 100, message = "El nombre de la colección no debe exceder los 100 caracteres")
    private String name;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer customerId;
}
