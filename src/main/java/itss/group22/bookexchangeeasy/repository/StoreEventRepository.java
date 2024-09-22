package itss.group22.bookexchangeeasy.repository;
import io.micrometer.observation.ObservationFilter;
import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface StoreEventRepository extends JpaRepository<StoreEvent, Long> {
    @Query("SELECT COUNT(u) FROM StoreEvent u WHERE YEAR(u.created) = ?1 AND MONTH(u.created) = ?2")
    Long countByMonth(int year, int month);

    Page<StoreEvent> findAllByOrderByCreatedDesc(Pageable pageable);
    @Query("SELECT p FROM  StoreEvent p " +
            "WHERE p.owner.id = ?1 " +
            "ORDER BY p.created DESC")
    Page<StoreEvent> findByUserOrderByCreatedDesc(Long userId, Pageable pageable);

    @Query("SELECT p FROM StoreEvent p " +
            "JOIN p.concernedUsers u " +  // Join with concernedUsers table
            "WHERE u.id = ?1 " +           // Filter by user ID
            "ORDER BY p.startTime DESC")
    Page<StoreEvent> findByConcernedUsersOrderByStartTimeDesc(Long userId, Pageable pageable);
    @Query("SELECT p FROM StoreEvent p " +
            "WHERE p.created >= ?1 AND p.created <= ?2 " +
            "ORDER BY p.created DESC")
    Page<StoreEvent> findByDateRangeOrderByStartTimeDesc(LocalDateTime from, LocalDateTime to, Pageable pageable);
}

