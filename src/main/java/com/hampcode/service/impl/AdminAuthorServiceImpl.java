package com.hampcode.service.impl;

import com.hampcode.dto.AuthorDTO;
import com.hampcode.exception.BadRequestException;
import com.hampcode.exception.ResourceNotFoundException;
import com.hampcode.mapper.AuthorMapper;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminAuthorServiceImpl implements AdminAuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDTO> getAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AuthorDTO> paginate(Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(authorMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDTO findById(Integer id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El autor con ID " + id + " no fue encontrado"));
        return authorMapper.toDto(author);
    }

    @Transactional
    @Override
    public AuthorDTO create(AuthorDTO authorDTO) {
        /*Optional<Author> existingAuthor = authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName());

        if (existingAuthor.isPresent()) {
            throw new BadRequestException("El autor ya existe con el mismo nombre y apellido");
        }*/

        authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName())
                .ifPresent(existingAuthor -> {
                    throw new BadRequestException("El autor ya existe con el mismo nombre y apellido");
                });

        Author author = authorMapper.toEntity(authorDTO);
        author.setCreatedAt(LocalDateTime.now());
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Transactional
    @Override
    public AuthorDTO update(Integer id, AuthorDTO updateAuthorDTO) {
        Author authorFromDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El autor con ID " + id + " no fue encontrado"));

        // Verificar si ya existe otro autor con el mismo nombre y apellido
        /*Optional<Author> existingAuthor = authorRepository.findByFirstNameAndLastName(updateAuthorDTO.getFirstName(), updateAuthorDTO.getLastName());

        if (existingAuthor.isPresent() && !existingAuthor.get().getId().equals(id)) {
            throw new BadRequestException("Ya existe un autor con el mismo nombre y apellido");
        }*/

        // Verificar si ya existe otro autor con el mismo nombre y apellido
        // Excluye al autor actual verificando que el ID sea diferente, para evitar tratar al mismo autor como duplicado
        authorRepository.findByFirstNameAndLastName(updateAuthorDTO.getFirstName(), updateAuthorDTO.getLastName())
                .filter(existingAuthor -> !existingAuthor.getId().equals(id))
                .ifPresent(existingAuthor -> {
                    throw new BadRequestException("Ya existe un autor con el mismo nombre y apellido");
                });

        // Actualizar los campos
        authorFromDb.setFirstName(updateAuthorDTO.getFirstName());
        authorFromDb.setLastName(updateAuthorDTO.getLastName());
        authorFromDb.setBio(updateAuthorDTO.getBio());
        authorFromDb.setUpdatedAt(LocalDateTime.now());

        authorFromDb = authorRepository.save(authorFromDb);
        return authorMapper.toDto(authorFromDb);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El autor con ID " + id + " no fue encontrado"));
        authorRepository.delete(author);
    }
}
