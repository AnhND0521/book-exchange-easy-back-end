package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void givenBooks_whenFindByAvailableStatusOrderByCreatedDesc_thenReturnAvailableBooksInDecreasingTimeOrder() {
        long minEpoch = LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.UTC) * 1000;  // 1 year in the past
        long maxEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;  // Current time
        bookRepository.saveAll(IntStream.range(0, 20).mapToObj(i -> {
            LocalDateTime created = LocalDateTime.ofInstant(Instant.ofEpochMilli(new Random().nextLong(minEpoch, maxEpoch)), ZoneOffset.UTC);
            return Book.builder().name("Book" + i).author("Author" + i).created(created).status(BookStatus.AVAILABLE).build();
        }).toList());

        var books = bookRepository.findByStatusOrderByCreatedDesc(BookStatus.AVAILABLE, PageRequest.of(0, 10)).toList();

        assertThat(books.size()).isEqualTo(10);
        for (int i = 0; i < books.size() - 1; i++) {
            assertThat(books.get(i).getCreated()).isAfterOrEqualTo(books.get(i + 1).getCreated());
        }
    }
}
