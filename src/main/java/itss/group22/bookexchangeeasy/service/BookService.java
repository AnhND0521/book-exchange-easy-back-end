package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.PostDTO;

import java.util.List;

public interface BookService {
    BookDTO postBook(BookDTO bookDTO);

    void updateBook(Long bookId, BookDTO bookDTO);

    void deleteBook(Long bookId);

    List<BookDTO> getLatestBooks(int page, int size);
}
