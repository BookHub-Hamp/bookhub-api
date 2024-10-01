package com.hampcode.mapper;

import com.hampcode.dto.BookCreateUpdateDTO;
import com.hampcode.dto.BookDetailsDTO;
import com.hampcode.model.entity.Book;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    private final ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Configurar ModelMapper para usar estrategia estricta
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // Mapeo de Book a BookDetailsDTO (para mostrar información completa)
    public BookDetailsDTO toDetailsDto(Book book) {
        BookDetailsDTO bookDetailsDTO = modelMapper.map(book, BookDetailsDTO.class);
        // Mapear manualmente el nombre del autor concatenando firstName y lastName
        bookDetailsDTO.setAuthorName(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        // Mapear manualmente el nombre de la categoría
        bookDetailsDTO.setCategoryName(book.getCategory().getName());
        return bookDetailsDTO;
    }

    // Mapeo de BookCreateUpdateDTO a Book (para crear/actualizar)
    public Book toEntity(BookCreateUpdateDTO bookCreateUpdateDTO) {
        return modelMapper.map(bookCreateUpdateDTO, Book.class);
    }

    // Mapeo de Book a BookCreateUpdateDTO (para casos donde necesites regresar el DTO de creación/actualización)
    public BookCreateUpdateDTO toCreateUpdateDto(Book book) {
        return modelMapper.map(book, BookCreateUpdateDTO.class);
    }
}
