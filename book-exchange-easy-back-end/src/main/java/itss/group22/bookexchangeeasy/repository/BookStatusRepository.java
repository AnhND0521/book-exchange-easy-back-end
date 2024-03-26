package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
}
