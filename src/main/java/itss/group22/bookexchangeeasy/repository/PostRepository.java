package itss.group22.bookexchangeeasy.repository;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("DELETE FROM Post p WHERE p.event.id = ?1")
    List<Post> deleteByEventId(Long eventId);
    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);
    @Query("SELECT p FROM Post p " +
            "WHERE p.user.id = ?1 " +
            "ORDER BY p.created DESC")
    Page<Post> findByUserOrderByTimestampDesc(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.event.id = ?1 " +
            "ORDER BY p.created DESC")
    Page<Post> findByEventOrderByTimestampDesc(Long eventId, Pageable pageable);
}

