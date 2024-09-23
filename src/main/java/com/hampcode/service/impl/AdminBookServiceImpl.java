package com.hampcode.service.impl;


import com.hampcode.dto.BookCreateUpdateDTO;
import com.hampcode.dto.BookDetailsDTO;
import com.hampcode.exception.BadRequestException;
import com.hampcode.exception.ResourceNotFoundException;
import com.hampcode.mapper.BookMapper;
import com.hampcode.model.entity.Author;
import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.Category;
import com.hampcode.repository.AuthorRepository;
import com.hampcode.repository.BookRepository;
import com.hampcode.repository.CategoryRepository;
import com.hampcode.service.AdminBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminBookServiceImpl implements AdminBookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BookDetailsDTO> findAll() {
        List<Book> books= bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDetailsDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookDetailsDTO> paginate(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDetailsDTO);
    }

    @Transactional
    @Override
    public BookDetailsDTO create(BookCreateUpdateDTO bookCreateUpdateDTO) {
        bookRepository.findByTitleOrSlug(bookCreateUpdateDTO.getTitle(), bookCreateUpdateDTO.getSlug())
                .ifPresent(book -> {
                    throw new BadRequestException("Ya existe un libro con el mismo titulo o slug");
                });

        // Asigna la categoría y el autor antes de guardar
        Category category = categoryRepository.findById(bookCreateUpdateDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookCreateUpdateDTO.getCategoryId()));
        Author author = authorRepository.findById(bookCreateUpdateDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookCreateUpdateDTO.getAuthorId()));

        Book book = bookMapper.toEntity(bookCreateUpdateDTO);

        book.setCategory(category);
        book.setAuthor(author);
        book.setCreatedAt(LocalDateTime.now());

        return bookMapper.toDetailsDTO(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public BookDetailsDTO findById(Integer id) {
        Book book = bookRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return bookMapper.toDetailsDTO(book);
    }

    @Transactional
    @Override
    public BookDetailsDTO update(Integer id, BookCreateUpdateDTO updatedBook) {
        Book bookFromDb = bookRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        bookRepository.findByTitleOrSlug(updatedBook.getTitle(), updatedBook.getSlug())
                .ifPresent(book -> {
                    throw new BadRequestException("Ya existe un libro con el mismo titulo o slug");
                });

        // Asigna la categoría y el autor antes de actualizar
        Category category = categoryRepository.findById(updatedBook.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + updatedBook.getCategoryId()));
        Author author = authorRepository.findById(updatedBook.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + updatedBook.getAuthorId()));

        // Actualización de los campos del libro
        //book = bookMapper.toEntity(updatedBook);

        bookFromDb.setTitle(updatedBook.getTitle());
        bookFromDb.setDescription(updatedBook.getDescription());
        bookFromDb.setPrice(updatedBook.getPrice());
        bookFromDb.setSlug(updatedBook.getSlug());
        bookFromDb.setCoverPath(updatedBook.getCoverPath());
        bookFromDb.setFilePath(updatedBook.getFilePath());
        bookFromDb.setCategory(category);
        bookFromDb.setAuthor(author);
        bookFromDb.setUpdatedAt(LocalDateTime.now());

        return bookMapper.toDetailsDTO(bookRepository.save(bookFromDb));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDetailsDTO> findTop6BooksByCreatedAt() {
        return bookRepository.findTop6ByOrderByCreatedAtDesc().stream()
                .map(bookMapper::toDetailsDto)
                .toList();
    }
}
