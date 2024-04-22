package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    List<ExchangeRequest> findByTargetBookIdOrderByTimestampAsc(Long id);
}
