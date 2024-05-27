package itss.group22.bookexchangeeasy.service;


import itss.group22.bookexchangeeasy.dto.book.BookDTO;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.enums.Gender;
import itss.group22.bookexchangeeasy.repository.*;
import itss.group22.bookexchangeeasy.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ExchangeOfferRepository exchangeOfferRepository;
    private ModelMapper mapper;
    private BookService bookService;
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        bookService = new BookServiceImpl(userRepository, bookRepository, categoryRepository, transactionRepository, exchangeOfferRepository, mapper,cloudinaryService);
    }

    @Test
    void givenValidBook_whenPostBook_thenBookIsSaved() {
        BookDTO bookDTO = BookDTO.builder()
                .ownerId(1L)
                .title("Book name")
                .author("Author X")
                .description("This is the description")
                .build();

        Long ownerId = 1L;
        User user = User.builder()
                .id(ownerId)
                .email("test@gmail.com")
                .name("Test User")
                .gender(Gender.MALE)
                .build();

        Book book = Book.builder()
                .id(1L)
                .owner(user)
                .title("Book name")
                .author("Author X")
                .description("This is the description")
                .status(BookStatus.AVAILABLE)
                .build();

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(user));

        when(bookRepository.save(any(Book.class)))
                .thenReturn(book);

        BookDTO returned = bookService.postBook(bookDTO);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());

        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook.getTitle()).isEqualTo("Book name");
        assertThat(capturedBook.getAuthor()).isEqualTo("Author X");
        assertThat(capturedBook.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(capturedBook.getOwner().getId()).isEqualTo(ownerId);
        assertThat(capturedBook.getOwner().getEmail()).isEqualTo("test@gmail.com");
        assertThat(capturedBook.getOwner().getName()).isEqualTo("Test User");

        assertThat(returned.getId()).isNotNull();
        assertThat(returned.getOwnerId()).isEqualTo(ownerId);
        assertThat(returned.getTitle()).isEqualTo("Book name");
        assertThat(returned.getAuthor()).isEqualTo("Author X");
        assertThat(returned.getStatus()).isEqualTo("AVAILABLE");
    }
}