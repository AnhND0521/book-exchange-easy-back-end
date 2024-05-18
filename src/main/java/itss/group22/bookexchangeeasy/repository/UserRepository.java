package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByOrderByEmailAsc(Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(u.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<User> findByEmailOrName(String keyword, Pageable pageable);
    @Query("SELECT COUNT(u) FROM User u WHERE YEAR(u.created) = ?1 AND MONTH(u.created) = ?2")
    Long countByMonth(int year, int month);
}
