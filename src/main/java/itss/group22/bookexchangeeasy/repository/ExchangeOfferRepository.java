package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.ExchangeOffer;
import itss.group22.bookexchangeeasy.enums.ExchangeOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeOfferRepository extends JpaRepository<ExchangeOffer, Long> {
    List<ExchangeOffer> findByTargetBookIdOrderByTimestampAsc(Long id);
    List<ExchangeOffer> findByTargetBookIdAndStatusOrderByTimestampAsc(Long id, ExchangeOfferStatus status);
    List<ExchangeOffer> findByStatus(ExchangeOfferStatus status);
}
