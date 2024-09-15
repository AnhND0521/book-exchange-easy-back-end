package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.community.comment.CreateCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.GetCommentResponse;
import itss.group22.bookexchangeeasy.entity.Comment;
import itss.group22.bookexchangeeasy.entity.CommentsPostsRef;
import itss.group22.bookexchangeeasy.repository.CommentRepository;
import itss.group22.bookexchangeeasy.repository.CommentsPostsRefRepository;
import itss.group22.bookexchangeeasy.service.CommentService;
import itss.group22.bookexchangeeasy.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentsPostsRefRepository commentsPostsRefRepository;
    private final CommentMapper commentMapper;

    @Override
    public void commentOnPost(Long postId, CreateCommentRequest request) {
        Comment comment = Comment.builder()
                .userId(request.getUserId())
                .content(request.getContent())
                .build();
        comment = commentRepository.save(comment);

        CommentsPostsRef commentsPostsRef = CommentsPostsRef.builder()
                .commentId(comment.getId())
                .postId(postId)
                .build();
        commentsPostsRefRepository.save(commentsPostsRef);
    }

    @Override
    public Page<GetCommentResponse> getCommentsOnPost(Long postId, int page, int size) {
        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, PageRequest.of(page, size));
        return comments.map(commentMapper::mapCommentToGetCommentResponse);
    }
}
