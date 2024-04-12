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

    @PostMapping("users/{userId}/books")
    public ResponseEntity<ResponseMessage> postBook(@PathVariable Long userId, @RequestBody @Valid PostDTO postDTO) {
        bookService.postBook(userId, postDTO);
        return ResponseEntity.ok(new ResponseMessage("Book posted successfully"));
    }

    @PutMapping("users/{userId}/books/{bookId}")
    public ResponseEntity<ResponseMessage> updateBook(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody @Valid BookDTO bookDTO) {
        bookService.updateBook(userId, bookId, bookDTO);
        return ResponseEntity.ok(new ResponseMessage("Book updated successfully"));
    }

    @DeleteMapping("users/{userId}/books/{bookId}")
    public ResponseEntity<ResponseMessage> deleteBook(@PathVariable Long userId, @PathVariable Long bookId) {
        bookService.deleteBook(userId, bookId);
        return ResponseEntity.ok(new ResponseMessage("Book deleted successfully"));
    }

    @GetMapping("/books")
    public ResponseEntity<List<PostDTO>> getAllBooks(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return ResponseEntity.ok(bookService.getAllBooks(page, size));
    }


}
