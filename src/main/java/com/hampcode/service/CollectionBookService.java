package com.hampcode.service;

import com.hampcode.dto.BookDetailsDTO;
import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.CollectionBook;

import java.time.LocalDateTime;
import java.util.List;

public interface CollectionBookService {

        CollectionBook addBookToCollection(Integer bookId, Integer collectionId);
    void removeBookFromCollection(Integer bookId, Integer collectionId);

    List<Book> getBooksInCollection(Integer collectionId);

}

