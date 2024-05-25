package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Category;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("SELECT c, COUNT(b) AS bookCount " +
            "FROM Book b " +
            "JOIN b.categories c " +
            "WHERE b.status = ?2 " +
            "GROUP BY c " +
            "ORDER BY bookCount DESC " +
            "LIMIT ?1")
    List<Object[]> findTopBookCategoriesWithBookStatus(int limit, BookStatus status);
}
