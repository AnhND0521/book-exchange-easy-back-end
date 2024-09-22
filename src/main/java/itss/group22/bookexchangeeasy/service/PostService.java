package itss.group22.bookexchangeeasy.service;
import itss.group22.bookexchangeeasy.dto.community.post.PostResponse;
import itss.group22.bookexchangeeasy.dto.community.post.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {
    PostResponse postPost(PostDTO postDTO);
    Page<PostResponse> getLatestPosts(int page, int size);
    PostResponse updatePost(Long postId, PostDTO postDTO);
    void deletePost(Long postId);
    Page<PostResponse> getPostsByUser(Long userId, int page, int size);
    Page<PostResponse> getPostsByEvent(Long eventId, int page, int size);
    String uploadPostImage(Long id, MultipartFile imageFile) throws IOException;
}
