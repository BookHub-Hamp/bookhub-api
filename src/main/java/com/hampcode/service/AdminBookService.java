package com.hampcode.service;

import com.hampcode.dto.BookCreateUpdateDTO;
import com.hampcode.dto.BookDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminBookService {
    List<BookDetailsDTO> findAll();
    Page<BookDetailsDTO> paginate(Pageable pageable);
    BookDetailsDTO findById(Integer id);
    BookDetailsDTO create(BookCreateUpdateDTO bookCreateUpdateDTO);
    BookDetailsDTO update(Integer id, BookCreateUpdateDTO updatedBookDTO);
    void delete(Integer id);

    List<BookDetailsDTO> findTop6BooksByCreatedAt();
}
