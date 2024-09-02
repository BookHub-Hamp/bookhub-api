package com.hampcode.api;

import com.hampcode.model.entity.Book;
import com.hampcode.service.AdminBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/books")
public class AdminBookController {
    private final AdminBookService adminBookService;

    @GetMapping
    public ResponseEntity<List<Book>> list() {
        List<Book> books = adminBookService.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Book>> paginate(
            @PageableDefault(size = 5, sort = "title") Pageable pageable) {
        Page<Book> page = adminBookService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book bookFormDTO) {
        Book createdBook = adminBookService.create(bookFormDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable Integer id) {
        Book book = adminBookService.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Integer id, @RequestBody Book bookFormDTO) {
        Book updatedBook = adminBookService.update(id, bookFormDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        adminBookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
