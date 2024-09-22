package itss.group22.bookexchangeeasy.service.mapper;

import itss.group22.bookexchangeeasy.dto.community.post.PostResponse;
import itss.group22.bookexchangeeasy.entity.CommentsPostsRef;
import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.repository.CommentsPostsRefRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentsPostsRefRepository commentsPostsRefRepository;

    public PostResponse mapPostToGetPostResponse(Post post) {
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imagePath(post.getImagePath())
                .created(post.getCreated())
                .build();

        if (Objects.nonNull(post.getLikedUsers())) {
            postResponse.setLikedUserIds(post.getLikedUsers().stream().map(User::getId).collect(Collectors.toSet()));
        } else {
            postResponse.setLikedUserIds(Set.of());
        }

        postResponse.setCommentCount(commentsPostsRefRepository.countByPostId(post.getId()));

        User user = post.getUser();
        postResponse.setUserId(user.getId());
        postResponse.setUserName(user.getName());
        postResponse.setUserPictureUrl(user.getPictureUrl());

        StoreEvent event = post.getEvent();
        if (Objects.nonNull(event)) {
            postResponse.setEventId(event.getId());
            postResponse.setEventName(event.getName());
        }

        return postResponse;
    }
}
