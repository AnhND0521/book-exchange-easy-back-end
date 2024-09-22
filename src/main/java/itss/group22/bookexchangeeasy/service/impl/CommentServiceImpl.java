package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.community.comment.CreateCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.GetCommentResponse;
import itss.group22.bookexchangeeasy.dto.community.comment.LikeUnlikeCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.UpdateCommentRequest;
import itss.group22.bookexchangeeasy.entity.Comment;
import itss.group22.bookexchangeeasy.entity.CommentsPostsRef;
import itss.group22.bookexchangeeasy.entity.CommentsReplyRef;
import itss.group22.bookexchangeeasy.entity.CommentsUsersLikeRef;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.CommentRepository;
import itss.group22.bookexchangeeasy.repository.CommentsPostsRefRepository;
import itss.group22.bookexchangeeasy.repository.CommentsReplyRefRepository;
import itss.group22.bookexchangeeasy.repository.CommentsUsersLikeRefRepository;
import itss.group22.bookexchangeeasy.service.CommentService;
import itss.group22.bookexchangeeasy.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentsPostsRefRepository commentsPostsRefRepository;
    private final CommentsReplyRefRepository commentsReplyRefRepository;
    private final CommentsUsersLikeRefRepository commentsUsersLikeRefRepository;
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

    @Override
    public void updateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        comment.setContent(request.getContent());
        comment.setIsEdited(true);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        commentsPostsRefRepository.deleteAllByCommentId(commentId);
        commentsReplyRefRepository.deleteAllByBaseCommentIdOrReplyCommentId(commentId, commentId);
        commentsUsersLikeRefRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void likeComment(Long commentId, LikeUnlikeCommentRequest request) {
        CommentsUsersLikeRef commentsUsersLikeRef = commentsUsersLikeRefRepository.findByCommentIdAndUserId(commentId, request.getUserId());
        if (Objects.nonNull(commentsUsersLikeRef)) {
            throw ApiException.USER_ALREADY_LIKED_COMMENT;
        }

        commentsUsersLikeRef = CommentsUsersLikeRef.builder()
                .commentId(commentId)
                .userId(request.getUserId())
                .build();
        commentsUsersLikeRefRepository.save(commentsUsersLikeRef);
    }

    @Override
    public void unlikeComment(Long commentId, LikeUnlikeCommentRequest request) {
        CommentsUsersLikeRef commentsUsersLikeRef = commentsUsersLikeRefRepository.findByCommentIdAndUserId(commentId, request.getUserId());
        if (Objects.isNull(commentsUsersLikeRef)) {
            throw ApiException.USER_DID_NOT_LIKE_COMMENT;
        }

        commentsUsersLikeRefRepository.delete(commentsUsersLikeRef);
    }

    @Override
    public void createReply(Long commentId, CreateCommentRequest request) {
        Comment replyComment = Comment.builder()
                .userId(request.getUserId())
                .content(request.getContent())
                .build();
        replyComment = commentRepository.save(replyComment);

        CommentsReplyRef commentsReplyRef = CommentsReplyRef.builder()
                .baseCommentId(commentId)
                .replyCommentId(replyComment.getId())
                .build();
        commentsReplyRefRepository.save(commentsReplyRef);
    }

    @Override
    public Page<GetCommentResponse> getReplies(Long commentId, int page, int size) {
        return commentRepository
                .findRepliesByBaseCommentIdOrderByCreatedAtAsc(commentId, PageRequest.of(page, size))
                .map(commentMapper::mapCommentToGetCommentResponse);
    }
}
