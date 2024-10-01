package com.hampcode.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data

public class BookDetailsDTO {

    private Integer id;
    private String title;
    private String slug;
    private String description;
    private Float price;
    private String coverPath;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private String categoryName;
    private String authorName;
}
