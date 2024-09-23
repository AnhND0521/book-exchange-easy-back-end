package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.book.BookDTO;
import itss.group22.bookexchangeeasy.dto.book.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookService {
    BookDTO postBook(BookDTO bookDTO);
    void updateBook(Long bookId, BookDTO bookDTO);
    void deleteBook(Long bookId);
    Page<BookDTO> getLatestBooks(int page, int size);
    BookDTO getBookDetails(Long bookId);
    List<BookDTO> getBookList(int page,int size);
    Page<BookDTO> searchBook(String keyword,int page,int size);
    Long getExchangedBooks(LocalDateTime fromDate, LocalDateTime toDate);
    Page<BookDTO> getBooksByUser(Long userId, int page, int size);
    List<CategoryDTO> listByCategories(Integer categories, Integer booksPerCategory);
    List<CategoryDTO> listByCategoriesV2(Integer categories, Integer booksPerCategory);

    Page<BookDTO> searchBookSortByAuthor(String keyword,int page,int size);
    Page<BookDTO> searchBookSortByTitle(String keyword,int page,int size);
    Page<BookDTO> getBooksByConcernedUser(Long userId, int page, int size);
    String uploadBookImage(Long id, MultipartFile imageFile) throws IOException;
}
