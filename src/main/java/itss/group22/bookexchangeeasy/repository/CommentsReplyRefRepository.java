package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.CommentsReplyRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsReplyRefRepository extends JpaRepository<CommentsReplyRef, Long> {
    long countByBaseCommentId(Long commentId);
    @Modifying
    void deleteAllByBaseCommentIdOrReplyCommentId(Long baseCommentId, Long replyCommentId);
}
