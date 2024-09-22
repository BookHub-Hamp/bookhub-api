package com.hampcode.api;

import com.hampcode.dto.AuthorDTO;
import com.hampcode.model.entity.Author;
import com.hampcode.service.AdminAuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/authors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // Aplicar la restricción a nivel de clase
public class AdminAuthorController {

    private final AdminAuthorService adminAuthorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> listAll() {
        List<AuthorDTO> authors = adminAuthorService.getAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<AuthorDTO>> paginate(@PageableDefault(size = 5, sort = "firstName")
                                                     Pageable pageable) {
        Page<AuthorDTO> page = adminAuthorService.paginate(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO createdAuthor = adminAuthorService.create(authorDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getById(@PathVariable Integer id) {
        AuthorDTO author = adminAuthorService.findById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Integer id,@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO updatedAuthor = adminAuthorService.update(id, authorDTO);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        adminAuthorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
