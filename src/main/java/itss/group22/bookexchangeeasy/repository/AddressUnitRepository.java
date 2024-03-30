package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.AddressUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressUnitRepository extends JpaRepository<AddressUnit, Long> {
}
