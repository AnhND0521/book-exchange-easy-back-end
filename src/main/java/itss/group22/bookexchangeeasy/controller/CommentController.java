package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.dto.common.ResponseMessage;
import itss.group22.bookexchangeeasy.dto.community.comment.CreateCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.GetCommentResponse;
import itss.group22.bookexchangeeasy.dto.community.comment.LikeUnlikeCommentRequest;
import itss.group22.bookexchangeeasy.dto.community.comment.UpdateCommentRequest;
import itss.group22.bookexchangeeasy.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ResponseMessage> commentOnPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        commentService.commentOnPost(postId, request);
        return ResponseEntity.ok(new ResponseMessage("Comment created successfully"));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<GetCommentResponse>> getCommentsOnPost(
            @PathVariable("postId") Long postId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentsOnPost(postId, page, size));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ResponseMessage> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid UpdateCommentRequest request
    ) {
        commentService.updateComment(commentId, request);
        return ResponseEntity.ok(new ResponseMessage("Comment updated successfully"));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseMessage> deleteComment(
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ResponseMessage("Comment deleted successfully"));
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<ResponseMessage> likeComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid LikeUnlikeCommentRequest request
    ) {
        commentService.likeComment(commentId, request);
        return ResponseEntity.ok(new ResponseMessage("Comment liked successfully"));
    }

    @PostMapping("/comments/{commentId}/unlike")
    public ResponseEntity<ResponseMessage> unlikeComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid LikeUnlikeCommentRequest request
    ) {
        commentService.unlikeComment(commentId, request);
        return ResponseEntity.ok(new ResponseMessage("Comment unliked successfully"));
    }
}
