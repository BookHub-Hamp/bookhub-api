package com.hampcode.api;

import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.CollectionBook;
import com.hampcode.service.CollectionBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/collections-books")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")  // Permitir solo a CUSTOMER y ADMIN
public class CollectionBookController {

    private final CollectionBookService collectionBookService;

    @PostMapping("/{collectionId}/add-book")
    public ResponseEntity<CollectionBook> addBookToCollection(@PathVariable Integer collectionId, @RequestParam Integer bookId) {
        CollectionBook collectionBook = collectionBookService.addBookToCollection(bookId, collectionId);
        return new ResponseEntity<>(collectionBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{collectionId}/remove-book/{bookId}")
    public ResponseEntity<Void> removeBookFromCollection(@PathVariable Integer collectionId, @PathVariable Integer bookId) {
        collectionBookService.removeBookFromCollection(bookId, collectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

   /* @GetMapping("/{collectionId}/books")
    public ResponseEntity<List<CollectionBook>> getBooksInCollection(@PathVariable Integer collectionId) {
        List<CollectionBook> collectionBooks = collectionBookService.getBooksInCollection(collectionId);
        return ResponseEntity.ok(collectionBooks);
    }*/

    @GetMapping("/{collectionId}/books")
    public ResponseEntity<List<Book>> getBooksInCollection(@PathVariable Integer collectionId) {
        List<Book> books = collectionBookService.getBooksInCollection(collectionId);
        return ResponseEntity.ok(books);
    }



}
