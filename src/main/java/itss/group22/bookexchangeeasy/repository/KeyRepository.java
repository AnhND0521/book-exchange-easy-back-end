package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
    Optional<Key> findByUserIdAndValueAndIsUsedAndExpireTimeAfter(Long userId, String value, Boolean isUsed, LocalDateTime time);
}
