package com.hampcode.service;

import com.hampcode.dto.BookDetailsDTO;
import java.util.List;

public interface BookService {
    List<BookDetailsDTO> findTop6BooksByCreatedAt();
}