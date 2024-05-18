package itss.group22.bookexchangeeasy.repository;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreEventRepository extends JpaRepository<StoreEvent, Long> {
    @Query("SELECT COUNT(u) FROM StoreEvent u WHERE YEAR(u.created) = ?1 AND MONTH(u.created) = ?2")
    Long countByMonth(int year, int month);
}
