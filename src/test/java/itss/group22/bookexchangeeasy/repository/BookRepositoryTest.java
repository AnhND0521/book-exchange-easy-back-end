package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Slf4j
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void givenBooks_whenFindByAvailableStatusOrderByCreatedDesc_thenReturnAvailableBooksInDecreasingTimeOrder() {
        bookRepository.saveAll(IntStream.range(0, 20).mapToObj(i ->
                Book.builder()
                        .title("Book" + i)
                        .author("Author" + i)
                        .created(RandomUtils.randomPastTime())
                        .status(BookStatus.AVAILABLE)
                        .build()
        ).toList());

        var books = bookRepository.findByStatusOrderByCreatedDesc(BookStatus.AVAILABLE, PageRequest.of(0, 10)).toList();

        assertThat(books.size()).isEqualTo(10);
        for (int i = 0; i < books.size() - 1; i++) {
            assertThat(books.get(i).getCreated()).isAfterOrEqualTo(books.get(i + 1).getCreated());
        }
    }

    @Test
    void givenBooks_whenCountByDate_thenReturnBooksAddedThatDate() {
        // given
        var books = IntStream.range(0, 20).mapToObj(i ->
                Book.builder()
                        .title("Book" + i)
                        .author("Author" + i)
                        .created(RandomUtils.randomPastTime(4))
                        .status(BookStatus.AVAILABLE)
                        .build()
        ).toList();
        log.info(books.stream().map(Book::getCreated).toList().toString());
        bookRepository.saveAll(books);
        log.info(books.stream().map(Book::getCreated).toList().toString());

        // when
        LocalDate date = books.get(0).getCreated().toLocalDate();
        long count = bookRepository.countByDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        log.info("count = " + count);

        // then
        long expected = bookRepository.findAll().stream().filter(b -> b.getCreated().toLocalDate().isEqual(date)).count();
        assertThat(count).isEqualTo(expected);
    }
}
