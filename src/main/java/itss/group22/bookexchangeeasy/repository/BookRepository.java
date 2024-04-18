package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByStatusOrderByCreatedDesc(BookStatus bookStatus, Pageable pageable);
}
