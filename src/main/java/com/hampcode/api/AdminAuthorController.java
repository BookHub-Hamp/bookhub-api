package com.hampcode.api;

import com.hampcode.model.entity.Author;
import com.hampcode.service.AdminAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/authors")
@RequiredArgsConstructor
public class AdminAuthorController {

    private final AdminAuthorService adminAuthorService;

    @GetMapping
    public ResponseEntity<List<Author>> listAll() {
        List<Author> authors = adminAuthorService.getAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Author>> paginate(@PageableDefault(size = 5, sort = "firstName")
                                                     Pageable pageable) {
        Page<Author> page = adminAuthorService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        Author createdAuthor = adminAuthorService.create(author);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Integer id) {
        Author author = adminAuthorService.findById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Integer id, @RequestBody Author author) {
        Author updatedAuthor = adminAuthorService.update(id, author);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        adminAuthorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
