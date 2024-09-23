package com.hampcode.api;

import com.hampcode.dto.BookDetailsDTO;
import com.hampcode.service.AdminBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class HomeBookController {

    private final AdminBookService adminBookService;

    // Endpoint para obtener los 6 libros más recientes
    @GetMapping("/recent")
    public ResponseEntity<List<BookDetailsDTO>> getRecentBooks() {
        List<BookDetailsDTO> recentBooks = adminBookService.findTop6BooksByCreatedAt();
        return new ResponseEntity<>(recentBooks, HttpStatus.OK);
    }
}