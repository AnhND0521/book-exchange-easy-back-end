package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
}
