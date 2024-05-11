package itss.group22.bookexchangeeasy.controller;
import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.ResponseMessage;
import itss.group22.bookexchangeeasy.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/books")
    @Operation(
            summary = "Đăng sách mới",
            description = "Chỉ có trường ownerId, name và author là bắt buộc, các trường khác là tùy chọn. " +
                    "Các trường id, status và created được sinh tự động nên không cần điền."
    )
    public ResponseEntity<BookDTO> postBook(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.postBook(bookDTO));
    }

    @PutMapping("/books/{bookId}")
    @Operation(
            summary = "Cập nhật sách",
            description = "Body tương tự đăng sách. Không cần điền các trường id, ownerId, status và created."
    )
    public ResponseEntity<ResponseMessage> updateBook(@PathVariable Long bookId, @RequestBody @Valid BookDTO bookDTO) {
        bookService.updateBook(bookId, bookDTO);
        return ResponseEntity.ok(new ResponseMessage("Book updated successfully"));
    }

    @DeleteMapping("/books/{bookId}")
    @Operation(summary = "Xóa sách")
    public ResponseEntity<ResponseMessage> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(new ResponseMessage("Book deleted successfully"));
    }

    @GetMapping("/books/latest")
    @Operation(
            summary = "Lấy những sách đăng gần đây nhất (có phân trang)"
    )
    public ResponseEntity<Page<BookDTO>> getLatestBooks(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(bookService.getLatestBooks(page, size));
    }

    @GetMapping("/books/{bookId}")
    @Operation(summary = "Lấy thông tin một cuốn sách")
    public ResponseEntity<BookDTO> getBookDetails(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetails(bookId));
    }
}
