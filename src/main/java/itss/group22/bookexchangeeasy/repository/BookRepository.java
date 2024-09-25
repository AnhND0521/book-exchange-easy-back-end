package itss.group22.bookexchangeeasy.repository;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
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


    @Query("SELECT b FROM Book b " +
            "JOIN b.concernedUsers u " +  // Join with concernedUsers table
            "WHERE u.id = ?1 " +           // Filter by user ID
            "ORDER BY b.created DESC")
    Page<Book> findByConcernedUsersOrderByStartTimeDesc(Long userId, Pageable pageable);

    Optional<Book> findByTitle(String title);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = ?1 AND b.status = ?2 ORDER BY RAND() LIMIT ?3")
    List<Book> findRandomByCategoryIdAndStatus(Long categoryId, BookStatus status, int limit);

    @Query("SELECT b FROM Book b JOIN b.owner o JOIN b.categories c " +
            "WHERE (?1 IS NULL OR b.title LIKE LOWER(CONCAT('%', ?1, '%'))) " +
            "AND (?2 IS NULL OR b.author LIKE LOWER(CONCAT('%', ?2, '%'))) " +
            "AND (?3 IS NULL OR b.publisher LIKE LOWER(CONCAT('%', ?3, '%'))) " +
            "AND (?4 IS NULL OR o.id = ?4) " +
            "AND (?5 IS NULL OR c.id = ?5)")
    Page<Book> findByTitleOrAuthorOrPublisherOrOwnerIdOrCategoryId(String title, String author, String publisher, Long ownerId, Long categoryId, Pageable pageable);
}
