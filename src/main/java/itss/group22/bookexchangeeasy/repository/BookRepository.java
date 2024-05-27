package itss.group22.bookexchangeeasy.repository;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
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
    Page<Book> findByTitleOrAuthor(String keyword, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', ?1, '%')) AND b.status = ?2 " +
            "ORDER BY b.author DESC") // Sort by author descending
    Page<Book> findByAuthorDESC(String keyword,BookStatus status, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', ?1, '%')) AND b.status = ?2 " +
            "ORDER BY b.title DESC") // Sort by title descending
    Page<Book> findByTitleDesc(String keyword, BookStatus status,Pageable pageable);

    List<Book> findByOwnerIdAndStatus(Long ownerId, BookStatus status);

    Long countByStatusAndCreatedBetween(BookStatus status, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT COUNT(b) FROM Book b WHERE YEAR(b.created) = ?1 AND MONTH(b.created) = ?2 AND DAY(b.created) = ?3")
    Long countByDate(int year, int month, int date);

    @Query("SELECT COUNT(b) FROM Book b WHERE YEAR(b.created) = ?1 AND MONTH(b.created) = ?2")
    Long countByMonth(int year, int month);

    Page<Book> findByOwnerIdOrderByCreatedDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = ?1 AND b.status = ?2 ORDER BY RAND()")
    Page<Book> findByCategoryIdAndStatus(Long categoryId, BookStatus status, Pageable pageable);


    @Query("SELECT b FROM Book b " +
            "JOIN b.concernedUsers u " +  // Join with concernedUsers table
            "WHERE u.id = ?1 " +           // Filter by user ID
            "ORDER BY b.created DESC")
    Page<Book> findByConcernedUsersOrderByStartTimeDesc(Long userId, Pageable pageable);
}
