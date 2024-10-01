package com.hampcode.service.impl;

import com.hampcode.dto.BookDetailsDTO;
import com.hampcode.mapper.BookMapper;
import com.hampcode.repository.BookRepository;
import com.hampcode.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookDetailsDTO> findTop6BooksByCreatedAt() {
        return bookRepository.findTop6ByOrderByCreatedAtDesc().stream()
                .map(bookMapper::toDetailsDto)
                .collect(Collectors.toList());
    }
}
