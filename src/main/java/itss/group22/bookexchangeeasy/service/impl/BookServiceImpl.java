package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.BookRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper mapper;

    @Override
    public BookDTO postBook(BookDTO bookDTO) {
        if (bookDTO.getOwnerId() == null)
            throw new ApiException("Owner id is required", HttpStatus.BAD_REQUEST);

        Book book = toEntity(bookDTO);
        book.setId(null);
        book = bookRepository.save(book);
        return toDTO(book);
    }

    @Override
    public void updateBook(Long bookId, BookDTO bookDTO) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("book", "id", bookId));
        bookDTO.setOwnerId(book.getOwner().getId());
        bookDTO.setCreated(book.getCreated());
        book = toEntity(bookDTO);
        book.setId(bookId);
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
        bookRepository.delete(book);
    }

    @Override
    public List<BookDTO> getLatestBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Book> books = bookRepository.findByStatusOrderByCreatedDesc(BookStatus.AVAILABLE, pageable);
        return books.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private Book toEntity(BookDTO bookDTO) {
        User user = userRepository.findById(bookDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", bookDTO.getOwnerId()));
        Book book = mapper.map(bookDTO, Book.class);
        if (bookDTO.getStatus() == null)
            book.setStatus(BookStatus.AVAILABLE);
        else
            book.setStatus(BookStatus.valueOf(bookDTO.getStatus()));
        book.setOwner(user);
        return book;
    }

    private BookDTO toDTO(Book book) {
        BookDTO dto = mapper.map(book, BookDTO.class);
        dto.setOwnerId(book.getOwner().getId());
        dto.setStatus(book.getStatus().name());
        return dto;
    }
}
