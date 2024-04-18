package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.PostDTO;
import itss.group22.bookexchangeeasy.dto.ResponseMessage;
import itss.group22.bookexchangeeasy.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<BookDTO> postBook(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.postBook(bookDTO));
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<ResponseMessage> updateBook(@PathVariable Long bookId, @RequestBody @Valid BookDTO bookDTO) {
        bookService.updateBook(bookId, bookDTO);
        return ResponseEntity.ok(new ResponseMessage("Book updated successfully"));
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<ResponseMessage> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(new ResponseMessage("Book deleted successfully"));
    }

    @GetMapping("/books/latest")
    public ResponseEntity<List<BookDTO>> getLatestBooks(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(bookService.getLatestBooks(page, size));
    }
}
