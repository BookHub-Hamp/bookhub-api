package com.hampcode.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CollectionDetailsDTO {

    private Integer id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String customerName;
}
