package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

//    @Query("SELECT p FROM Post p "
//            + "JOIN p.book b "
//            + "WHERE b.status = ?1 "
//            + "ORDER BY p.created DESC")
//    List<Post> getAllPost(BookStatus status, Pageable pageable);
}

