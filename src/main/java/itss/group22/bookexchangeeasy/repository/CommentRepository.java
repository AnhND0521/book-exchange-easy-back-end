package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "JOIN CommentsPostsRef cp ON cp.commentId = c.id " +
            "WHERE cp.postId = ?1 " +
            "ORDER BY c.createdAt DESC")
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "JOIN CommentsReplyRef cr ON cr.replyCommentId = c.id " +
            "WHERE cr.baseCommentId = ?1 " +
            "ORDER BY c.createdAt ASC")
    Page<Comment> findRepliesByBaseCommentIdOrderByCreatedAtAsc(Long baseCommentId, Pageable pageable);
}
