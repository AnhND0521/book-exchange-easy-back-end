package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.CommentsPostsRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsPostsRefRepository extends JpaRepository<CommentsPostsRef, Long> {
    @Modifying
    void deleteAllByCommentId(Long commentId);
}
