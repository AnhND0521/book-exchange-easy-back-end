package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.StoreEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreEventRepository extends JpaRepository<StoreEvent, Long>
{

}
