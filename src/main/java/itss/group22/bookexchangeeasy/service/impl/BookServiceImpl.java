package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.book.BookDTO;
import itss.group22.bookexchangeeasy.dto.book.CategoryDTO;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.Category;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.*;
import itss.group22.bookexchangeeasy.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final ExchangeOfferRepository exchangeOfferRepository;
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
        if (book.getStatus().equals(BookStatus.EXCHANGED))
            throw new ApiException("The book can't be deleted as it is currently in an exchange");

        var transactions = transactionRepository.findByTargetBookId(bookId);
        transactionRepository.saveAll(transactions.stream().peek(transaction -> transaction.setTargetBook(null)).toList());

        var requests = exchangeOfferRepository.findByTargetBookIdOrderByTimestampAsc(bookId);
        exchangeOfferRepository.deleteAll(requests);

        book.setCategories(null);
        book = bookRepository.save(book);
        bookRepository.delete(book);
    }

    @Override
    public Page<BookDTO> getLatestBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findByStatusOrderByCreatedDesc(BookStatus.AVAILABLE, pageable);
        return books.map(this::toDTO);
    }

    @Override
    public BookDTO getBookDetails(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
        return toDTO(book);
    }

    @Override
    public List<BookDTO> getBookList(int page, int size) {
        return bookRepository.findAllByOrderByCreatedDesc(PageRequest.of(page, size)).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<BookDTO> searchBook(String keyword, int page, int size) {
        return bookRepository.findByTitleOrAuthor(keyword, PageRequest.of(page, size)).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Long getExchangedBooks(LocalDateTime fromDate, LocalDateTime toDate) {
        BookStatus exchangedStatus = BookStatus.EXCHANGED; // Assuming exchanged books have this status
        return bookRepository.countByStatusAndCreatedBetween(exchangedStatus, fromDate, toDate);
    }

    @Override
    public Page<BookDTO> getBooksByUser(Long userId, int page, int size) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return bookRepository
                .findByOwnerIdOrderByCreatedDesc(userId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    @Override
    public List<CategoryDTO> listByCategories(Integer categories, Integer booksPerCategory) {
        return categoryRepository.findTopBookCategoriesWithBookStatus(categories, BookStatus.AVAILABLE)
                .stream()
                .map(result -> {
                    Category category = (Category) result[0];
                    Page<Book> books = bookRepository.findByCategoryIdAndStatus(category.getId(), BookStatus.AVAILABLE, PageRequest.of(0, booksPerCategory));
                    return CategoryDTO.builder()
                            .id(category.getId())
                            .categoryName(category.getName())
                            .books(books.map(this::toDTO).toList())
                            .build();
                }).toList();
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

        if (bookDTO.getCategories() != null && bookDTO.getCategories().size() > 0) {
            List<Category> categories = bookDTO.getCategories().stream()
                    .map(name -> {
                        Optional<Category> category = categoryRepository.findByName(name);
                        if (category.isPresent())
                            return category.get();
                        return categoryRepository.save(Category.builder().name(name).build());
                    }).toList();
            book.setCategories(categories);
        }

        return book;
    }

    private BookDTO toDTO(Book book) {
        BookDTO dto = mapper.map(book, BookDTO.class);
        dto.setOwnerId(book.getOwner().getId());
        dto.setStatus(book.getStatus().name());
        dto.setCategories(book.getCategories().stream().map(Category::getName).toList());
        return dto;
    }

}
