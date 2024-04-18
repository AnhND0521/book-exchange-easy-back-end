package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
}

