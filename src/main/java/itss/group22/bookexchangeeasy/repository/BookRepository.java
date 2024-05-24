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

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Book> findByTitleOrAuthor(String keyword, Pageable pageable);

    List<Book> findByOwnerIdAndStatus(Long ownerId, BookStatus status);

    Long countByStatusAndCreatedBetween(BookStatus status, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT COUNT(b) FROM Book b WHERE YEAR(b.created) = ?1 AND MONTH(b.created) = ?2 AND DAY(b.created) = ?3")
    Long countByDate(int year, int month, int date);

    @Query("SELECT COUNT(b) FROM Book b WHERE YEAR(b.created) = ?1 AND MONTH(b.created) = ?2")
    Long countByMonth(int year, int month);

    Page<Book> findByOwnerIdOrderByCreatedDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = ?1 ORDER BY RAND()")
    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);
}
