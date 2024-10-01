package com.hampcode.mapper;


import com.hampcode.model.entity.Author;
import com.hampcode.dto.AuthorDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    private final ModelMapper modelMapper;

    public AuthorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AuthorDTO toDto(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }

    public Author toEntity(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }
}