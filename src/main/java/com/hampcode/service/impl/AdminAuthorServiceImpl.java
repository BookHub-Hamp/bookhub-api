package com.hampcode.service.impl;

import com.hampcode.model.entity.Author;
import com.hampcode.repository.AuthorRepository;
import com.hampcode.service.AdminAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminAuthorServiceImpl implements AdminAuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Author> paginate(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Author findById(Integer id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Transactional
    @Override
    public Author create(Author author) {
        author.setCreatedAt(LocalDateTime.now());
        return authorRepository.save(author);
    }

    @Transactional
    @Override
    public Author update(Integer id, Author updateAuthor) {
        Author authorFromDb = findById(id);
        authorFromDb.setFirstName(updateAuthor.getFirstName());
        authorFromDb.setLastName(updateAuthor.getLastName());
        authorFromDb.setBio(updateAuthor.getBio());
        authorFromDb.setUpdatedAt(LocalDateTime.now());
        return authorRepository.save(authorFromDb);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Author author = findById(id);
        authorRepository.delete(author);
    }
}
