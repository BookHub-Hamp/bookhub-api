package com.hampcode.mapper;

import com.hampcode.dto.BookCreateUpdateDTO;
import com.hampcode.dto.BookDetailsDTO;
import com.hampcode.model.entity.Book;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    private final ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        //Configurar ModelMapper para usar estrategia de coincidencia estricta
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public BookDetailsDTO toDetailsDTO(Book book) {
        BookDetailsDTO bookDetailsDTO =  modelMapper.map(book, BookDetailsDTO.class);

        bookDetailsDTO.setAuthorName(book.getAuthor().getFirstName()+" "+book.getAuthor().getLastName());
        bookDetailsDTO.setCategoryName(book.getCategory().getName());

        return bookDetailsDTO;
    }

    public Book toEntity(BookCreateUpdateDTO bookCreateUpdateDTO) {
        return modelMapper.map(bookCreateUpdateDTO, Book.class);
    }

    public BookCreateUpdateDTO toCreateUpdateDTO(Book book) {
        return modelMapper.map(book, BookCreateUpdateDTO.class);
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

}