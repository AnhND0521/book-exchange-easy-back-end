package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.CommentsUsersLikeRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsUsersLikeRefRepository extends JpaRepository<CommentsUsersLikeRef, Long> {
    long countByCommentId(Long commentId);
    @Modifying
    void deleteAllByCommentId(Long commentId);
}
