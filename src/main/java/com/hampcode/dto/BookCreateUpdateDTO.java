package com.hampcode.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookCreateUpdateDTO {
    private Integer id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título debe tener 255 caracteres o menos")
    private String title;

    @NotBlank(message = "El slug es obligatorio")
    @Size(max = 255, message = "El slug debe tener 255 caracteres o menos")
    private String slug;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    private Float price;

    private String coverPath;
    private String filePath;

    @NotNull(message = "La categoría es obligatoria")
    private Integer categoryId;

    @NotNull(message = "El autor es obligatorio")
    private Integer authorId;
}
