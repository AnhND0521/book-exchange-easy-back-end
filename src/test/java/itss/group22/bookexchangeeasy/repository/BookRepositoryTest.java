package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.utils.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

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
        bookRepository.saveAll(IntStream.range(0, 20).mapToObj(i ->
                Book.builder()
                        .name("Book" + i)
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
}
