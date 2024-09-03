package com.hampcode.service;

import com.hampcode.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminBookService {
    List<Book> findAll();
    Page<Book> paginate(Pageable pageable);
    Book create(Book book);
    Book findById(Integer id);
    Book update(Integer id, Book updatedBook);
    void delete(Integer id);
}
