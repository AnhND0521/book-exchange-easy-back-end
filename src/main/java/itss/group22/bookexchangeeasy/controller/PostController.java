package itss.group22.bookexchangeeasy.controller;

import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.common.ResponseMessage;
import itss.group22.bookexchangeeasy.dto.community.post.PostResponse;
import itss.group22.bookexchangeeasy.dto.community.post.PostDTO;
import itss.group22.bookexchangeeasy.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    @Operation(summary = "Đăng một bài đăng")
    public ResponseEntity<PostResponse> postPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.postPost(postDTO));
    }
    @GetMapping("/posts/latest")
    @Operation(
            summary = "Lấy những bài đăng gần đây nhất (có phân trang)"
    )
    public ResponseEntity<Page<PostResponse>> getLatestPosts(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(postService.getLatestPosts(page, size));
    }
    @PutMapping("/posts/{postId}")
    @Operation(
            summary = "Cập nhật bài đăng",
            description = "Body tương tự đăng sự kiện. Không cần điền các trường id và created."
    )
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody @Valid PostDTO postDTO) {
        postService.updatePost(postId, postDTO);
        return ResponseEntity.ok(postService.updatePost(postId, postDTO));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Xóa bài đăng")
    public ResponseEntity<ResponseMessage> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(new ResponseMessage("Post deleted successfully"));
    }
    @GetMapping("/posts/find-by-user")
    @Operation(summary = "Lấy danh sách các bài đăng mà một người dùng đăng (có phân trang)")
    public ResponseEntity<Page<PostResponse>> getPostsByUser(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(postService.getPostsByUser(userId, page, size));
    }
    @GetMapping("/posts/find-by-event")
    @Operation(summary = "Lấy danh sách các bài đăng thuộc cùng 1 event(có phân trang)")
    public ResponseEntity<Page<PostResponse>> getPostsByEvent(
            @RequestParam(name = "id", required = true) Long eventId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(postService.getPostsByEvent(eventId, page, size));
    }
    @PostMapping("/posts/{id}/upload-image-post")
    @Operation(summary = "Upload ảnh của post")
    private ResponseEntity<ResponseMessage> uploadPostImage(@PathVariable Long id, @RequestParam MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(new ResponseMessage(postService.uploadPostImage(id, imageFile)));
    }

}
