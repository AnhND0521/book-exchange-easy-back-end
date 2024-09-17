package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.community.comment.CreateCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.GetCommentResponse;
import itss.group22.bookexchangeeasy.dto.community.comment.LikeUnlikeCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.UpdateCommentRequest;
import org.springframework.data.domain.Page;

public interface CommentService {
    void commentOnPost(Long postId, CreateCommentRequest request);
    Page<GetCommentResponse> getCommentsOnPost(Long postId, int page, int size);
    void updateComment(Long commentId, UpdateCommentRequest request);
    void deleteComment(Long commentId);
    void likeComment(Long commentId, LikeUnlikeCommentRequest request);
    void unlikeComment(Long commentId, LikeUnlikeCommentRequest request);
    void createReply(Long commentId, CreateCommentRequest request);
    Page<GetCommentResponse> getReplies(Long commentId, int page, int size);
}
