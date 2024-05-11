package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.PostDTO;
import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.PostRepository;
import itss.group22.bookexchangeeasy.repository.StoreEventRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StoreEventRepository eventRepository;
    private final PostRepository postRepository;
    @Override
    public PostDTO postPost(PostDTO postDTO) {
        Post post = toEntity(postDTO);
        post.setId(null);
        post.setLikedUsers(null);
        post = postRepository.save(post);
        return toDTO(post);
    }
    private PostDTO toDTO(Post post) {
        PostDTO postDTO = mapper.map(post, PostDTO.class);
        postDTO.setUserId(post.getUser().getId());
        postDTO.setLikedUserIds(post.getLikedUsers().stream().map(user -> user.getId()).collect(Collectors.toSet()));
        if(post.getEvent() != null){
            postDTO.setEventId(post.getEvent().getId());
        }
        return postDTO;
    }
    private Post toEntity(PostDTO postDTO) {
        Post post = mapper.map(postDTO, Post.class);
        post.setUser(userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", postDTO.getUserId())));
        post.setLikedUsers(postDTO.getLikedUserIds().stream().map(userId -> userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId))).collect(Collectors.toSet()));
        if(postDTO.getEventId() != null){
            post.setEvent(eventRepository.findById(postDTO.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", postDTO.getEventId())));
        }
        return post;
    }

}

