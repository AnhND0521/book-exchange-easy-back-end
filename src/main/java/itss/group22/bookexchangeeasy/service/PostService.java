package itss.group22.bookexchangeeasy.service;
import itss.group22.bookexchangeeasy.dto.community.PostDTO;
import org.springframework.data.domain.Page;

public interface PostService {
    PostDTO postPost(PostDTO postDTO);
    Page<PostDTO> getLatestPosts(int page, int size);
    void updatePost(Long postId, PostDTO postDTO);
    void deletePost(Long postId);
    Page<PostDTO> getPostsByUser(Long userId, int page, int size);
    Page<PostDTO> getPostsByEvent(Long eventId, int page, int size);
}
