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
                .map(authorMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AuthorDTO> paginate(Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(authorMapper::toDTO);
    }


    @Transactional(readOnly = true)
    @Override
    public AuthorDTO findById(Integer id) {
       Author author = authorRepository.findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("El autor con ID "+id+" no fue encontrado"));
       return authorMapper.toDTO(author);
    }

    @Transactional
    @Override
    public AuthorDTO create(AuthorDTO authorDTO) {
        authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName())
                        .ifPresent(existingAuthor ->{
                            throw new BadRequestException("El autor ya existe con el mismo nombre y apellido");
                        });

        Author author = authorMapper.toEntity(authorDTO);
        author.setCreatedAt(LocalDateTime.now());
        author = authorRepository.save(author);
        return authorMapper.toDTO(author);
    }

    @Transactional
    @Override
    public AuthorDTO update(Integer id, AuthorDTO updateAuthorDTO) {
        Author authorFromDb = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El autor con ID " + id + " no fue encontrado"));


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
        return authorMapper.toDTO(authorFromDb);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El autor con ID " + id + " no fue encontrado"));
        authorRepository.delete(author);
    }
}
