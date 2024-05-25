package itss.group22.bookexchangeeasy.controller;

import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.book.BookDTO;
import itss.group22.bookexchangeeasy.dto.book.CategoryDTO;
import itss.group22.bookexchangeeasy.dto.book.TransactionDTO;
import itss.group22.bookexchangeeasy.dto.common.ResponseMessage;
import itss.group22.bookexchangeeasy.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/books")
    @Operation(summary = "Lấy danh sách tất cả sách")
    private ResponseEntity<List<BookDTO>> getBookList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(bookService.getBookList(page, size));
    }

    @GetMapping("/books/search")
    @Operation(summary = "Tìm kiếm sách")
    private ResponseEntity<List<BookDTO>> searchBook(
            @RequestParam(name = "q") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(bookService.searchBook(keyword, page, size));
    }
    @GetMapping("/books/search-sort-by-author")
    @Operation(summary = "Tìm kiếm sách sắp xếp theo tên tác giả")
    private ResponseEntity<Page<BookDTO>> searchBookSortByAuthor(
            @RequestParam(name = "q") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size
    ) {
        return ResponseEntity.ok((Page<BookDTO>) bookService.searchBookSortByAuthor(keyword, page, size));
    }

    @GetMapping("/books/search-sort-by-title")
    @Operation(summary = "Tìm kiếm sách sắp xếp theo tên sach")
    private ResponseEntity<Page<BookDTO>> searchBookSortByTitle(
            @RequestParam(name = "q") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size
    ) {
        return ResponseEntity.ok((Page<BookDTO>) bookService.searchBookSortByTitle(keyword, page, size));
    }

    @GetMapping("books/find-by-user")
    @Operation(summary = "Lấy danh sách các cuốn sách mà một người dùng đăng (có phân trang)")
    public ResponseEntity<Page<BookDTO>> getBooksByUser(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(bookService.getBooksByUser(userId, page, size));
    }

    @GetMapping("/books/list-by-categories")
    @Operation(summary = "Liệt kê sách theo từng thể loại")
    public ResponseEntity<List<CategoryDTO>> listByCategories(
            @RequestParam(name = "categories", required = false, defaultValue = "5") Integer categories,
            @RequestParam(name = "books-per-category", required = false, defaultValue = "20") Integer booksPerCategories
    ) {
        return ResponseEntity.ok(bookService.listByCategories(categories, booksPerCategories));
    }
}
