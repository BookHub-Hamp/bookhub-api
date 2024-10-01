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
import java.util.stream.Collectors;

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
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDetailsDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookDetailsDTO> paginate(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDetailsDto);
    }

    @Transactional(readOnly = true)
    @Override
    public BookDetailsDTO findById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return bookMapper.toDetailsDto(book);
    }

    @Transactional
    @Override
    public BookDetailsDTO create(BookCreateUpdateDTO bookCreateUpdateDTO) {
        bookRepository.findByTitleOrSlug(bookCreateUpdateDTO.getTitle(), bookCreateUpdateDTO.getSlug())
                .ifPresent(existingBook -> {
                    throw new BadRequestException("Ya existe un libro con el mismo título o slug");
                });

        Category category = categoryRepository.findById(bookCreateUpdateDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookCreateUpdateDTO.getCategoryId()));
        Author author = authorRepository.findById(bookCreateUpdateDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookCreateUpdateDTO.getAuthorId()));

        Book book = bookMapper.toEntity(bookCreateUpdateDTO);
        book.setCategory(category);
        book.setAuthor(author);
        book.setCreatedAt(LocalDateTime.now());

        return bookMapper.toDetailsDto(bookRepository.save(book));
    }


    @Transactional
    @Override
    public BookDetailsDTO update(Integer id, BookCreateUpdateDTO updatedBookDTO) {
        Book bookFromDb = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con el ID: " + id));

        bookRepository.findByTitleOrSlug(updatedBookDTO.getTitle(), updatedBookDTO.getSlug())
                .filter(existingBook -> !existingBook.getId().equals(id))
                .ifPresent(existingBook -> {
                    throw new BadRequestException("Ya existe un libro con el mismo título o slug");
                });

        Category category = categoryRepository.findById(updatedBookDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + updatedBookDTO.getCategoryId()));
        Author author = authorRepository.findById(updatedBookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + updatedBookDTO.getAuthorId()));

        // Actualizar los campos del libro
        bookFromDb.setTitle(updatedBookDTO.getTitle());
        bookFromDb.setDescription(updatedBookDTO.getDescription());
        bookFromDb.setPrice(updatedBookDTO.getPrice());
        bookFromDb.setSlug(updatedBookDTO.getSlug());
        bookFromDb.setCoverPath(updatedBookDTO.getCoverPath());
        bookFromDb.setFilePath(updatedBookDTO.getFilePath());
        bookFromDb.setCategory(category);
        bookFromDb.setAuthor(author);
        bookFromDb.setUpdatedAt(LocalDateTime.now());

        return bookMapper.toDetailsDto(bookRepository.save(bookFromDb));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    @Override
    public List<BookDetailsDTO> findTop6BooksByCreatedAt() {
        return bookRepository.findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(bookMapper::toDetailsDto)
                .toList();
    }
}
