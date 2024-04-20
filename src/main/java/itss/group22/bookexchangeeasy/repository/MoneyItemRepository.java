package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.MoneyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyItemRepository extends JpaRepository<MoneyItem, Long> {

}
