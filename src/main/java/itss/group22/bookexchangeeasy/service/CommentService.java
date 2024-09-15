package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.community.comment.CreateCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.GetCommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    void commentOnPost(Long postId, CreateCommentRequest request);
    Page<GetCommentResponse> getCommentsOnPost(Long postId, int page, int size);
}
