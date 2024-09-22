package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.community.post.PostResponse;
import itss.group22.bookexchangeeasy.dto.community.post.PostDTO;
import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.PostRepository;
import itss.group22.bookexchangeeasy.repository.StoreEventRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.CloudinaryService;
import itss.group22.bookexchangeeasy.service.PostService;
import itss.group22.bookexchangeeasy.service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StoreEventRepository eventRepository;
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    private final PostMapper postMapper;

    @Override
    public PostResponse postPost(PostDTO postDTO) {
        Post post = toEntity(postDTO);
        post.setId(null);
        post.setLikedUsers(null);
        post = postRepository.save(post);
        return postMapper.mapPostToGetPostResponse(post);
    }

    @Override
    public Page<PostResponse> getLatestPosts(int page, int size) {
        return postRepository.findAllByOrderByCreatedDesc(PageRequest.of(page, size)).map(postMapper::mapPostToGetPostResponse);
    }

    @Override
    public PostResponse updatePost(Long postId, PostDTO postDTO) {
        Post post =postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        postDTO.setUserId(post.getUser().getId());
        postDTO.setCreated(post.getCreated());
        post = toEntity(postDTO);
        post.setId(postId);
        postRepository.save(post);
        return postMapper.mapPostToGetPostResponse(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id",postId));
        post.setLikedUsers(null);
        post = postRepository.save(post);
        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> getPostsByUser(Long userId, int page, int size) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return postRepository
                .findByUserOrderByTimestampDesc(userId, PageRequest.of(page, size))
                .map(postMapper::mapPostToGetPostResponse);
    }

    @Override
    public Page<PostResponse> getPostsByEvent(Long eventId, int page, int size) {
        eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        return postRepository
                .findByEventOrderByTimestampDesc(eventId, PageRequest.of(page, size))
                .map(postMapper::mapPostToGetPostResponse);
    }

    @Override
    public String uploadPostImage(Long id, MultipartFile imageFile) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        Map data =  cloudinaryService.uploadFile(imageFile);
        post.setImagePath(data.get("url").toString());
        postRepository.save(post);
        return data.get("url").toString();
    }

    private Post toEntity(PostDTO postDTO) {
        Post post = mapper.map(postDTO, Post.class);
        post.setUser(userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", postDTO.getUserId())));
        if(postDTO.getLikedUserIds() != null){
            post.setLikedUsers(postDTO.getLikedUserIds().stream().map(userId -> userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId))).collect(Collectors.toSet()));
        }
        if(postDTO.getEventId() != null){
            post.setEvent(eventRepository.findById(postDTO.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", postDTO.getEventId())));
        }
        return post;
    }


}

