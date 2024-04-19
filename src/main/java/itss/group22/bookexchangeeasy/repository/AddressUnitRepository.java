package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.AddressUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressUnitRepository extends JpaRepository<AddressUnit, String> {
    List<AddressUnit> findByTypeOrderByNameAsc(Integer type);
    List<AddressUnit> findByParentIdOrderByNameAsc(String parentId);
}
