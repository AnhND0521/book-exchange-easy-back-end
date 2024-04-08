package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}
