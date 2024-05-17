package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByStatusOrderByCreatedDesc(BookStatus bookStatus, Pageable pageable);
    List<Book> findAllByOrderByCreatedDesc(Pageable pageable);
    @Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Book> findByNameOrAuthor(String keyword, Pageable pageable);
    Long countByStatusAndCreatedDateBetween(BookStatus status, LocalDateTime fromDate, LocalDateTime toDate);
}
