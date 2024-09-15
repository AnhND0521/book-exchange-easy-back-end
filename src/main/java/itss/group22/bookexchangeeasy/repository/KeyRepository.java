package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Key;
import itss.group22.bookexchangeeasy.enums.KeyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
    Optional<Key> findByValueAndIsUsedAndExpireTimeAfter(String value, Boolean isUsed, LocalDateTime time);
    Optional<Key> findByValueAndKeyTypeAndIsUsedAndExpireTimeAfter(String value, KeyType keyType, Boolean isUsed, LocalDateTime time);
}
