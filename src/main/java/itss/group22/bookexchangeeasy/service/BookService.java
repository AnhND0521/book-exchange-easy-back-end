package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    BookDTO postBook(BookDTO bookDTO);
    void updateBook(Long bookId, BookDTO bookDTO);
    void deleteBook(Long bookId);
    Page<BookDTO> getLatestBooks(int page, int size);
    BookDTO getBookDetails(Long bookId);
    List<BookDTO> getBookList(int page,int size);
}
