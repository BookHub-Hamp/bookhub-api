package com.hampcode.service.impl;

import com.hampcode.exception.BadRequestException;
import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.Collection;
import com.hampcode.model.entity.CollectionBook;
import com.hampcode.model.entity.CollectionBookPK;
import com.hampcode.repository.CollectionBookRepository;
import com.hampcode.repository.CollectionRepository;
import com.hampcode.service.CollectionBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionBookServiceImpl implements CollectionBookService {

    private final CollectionBookRepository collectionBookRepository;
    private final CollectionRepository collectionRepository; // Asegúrate de tener acceso al repo de Collection

    @Override
    @Transactional
    public CollectionBook addBookToCollection(Integer bookId, Integer collectionId) {
        // Verificar si el libro ya está en la colección
        if (collectionBookRepository.existsByBookAndCollection(bookId, collectionId)) {
            throw new BadRequestException("Este libro ya está en la colección.");
        }

        // Establecer la fecha en que se agrega el libro
        LocalDateTime addedDate = LocalDateTime.now();

        // Agregar el libro a la colección
        collectionBookRepository.addBookToCollection(bookId, collectionId, addedDate);

        // Crear el objeto para devolver
        CollectionBook collectionBook = new CollectionBook();
        collectionBook.setBook(bookId);
        collectionBook.setCollection(collectionId);
        collectionBook.setAddedDate(addedDate);

        return collectionBook;
    }


    @Override
    @Transactional
    public void removeBookFromCollection(Integer bookId, Integer collectionId) {
        collectionBookRepository.deleteByBookAndCollection(bookId, collectionId);
    }

    /*
    @Override
    public List<CollectionBook> getBooksInCollection(Integer collectionId) {
        return collectionBookRepository.findByCollection(collectionId);
    }*/

    @Override
    public List<Book> getBooksInCollection(Integer collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));
        return collectionBookRepository.findBooksByCollection(collection);
    }

   /* @Override
   public List<Book> getBooksInCollection(Integer collectionId) {
        // Asegúrate de que la colección existe si es necesario
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        // Llama al repositorio con el ID de colección en lugar del objeto Collection
        return collectionBookRepository.findBooksByCollection(collectionId);
    }*/

}
