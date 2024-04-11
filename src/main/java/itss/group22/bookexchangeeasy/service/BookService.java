package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.PostDTO;

import java.util.List;

public interface BookService {
void postBook(Long userId, PostDTO postDTO);
void updateBook(Long userId, Long bookId, BookDTO bookDTO);
void deleteBook(Long userId, Long bookId);
List<PostDTO> getAllBooks(int page, int size);
}
