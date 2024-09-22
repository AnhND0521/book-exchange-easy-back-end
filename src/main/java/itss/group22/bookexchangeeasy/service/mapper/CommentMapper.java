package itss.group22.bookexchangeeasy.service.mapper;

import itss.group22.bookexchangeeasy.dto.community.comment.GetCommentResponse;
import itss.group22.bookexchangeeasy.entity.Comment;
import itss.group22.bookexchangeeasy.entity.CommentsUsersLikeRef;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.repository.CommentsReplyRefRepository;
import itss.group22.bookexchangeeasy.repository.CommentsUsersLikeRefRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentsUsersLikeRefRepository commentsUsersLikeRefRepository;
    @Autowired
    private CommentsReplyRefRepository commentsReplyRefRepository;

    public GetCommentResponse mapCommentToGetCommentResponse(Comment comment) {
        User user = userRepository.findById(comment.getUserId()).orElseThrow();
        List<CommentsUsersLikeRef> commentsUsersLikeRefs = commentsUsersLikeRefRepository.findByCommentId(comment.getId());

        return GetCommentResponse.builder()
                .id(comment.getId())
                .userId(user.getId())
                .userName(user.getName())
                .userPictureUrl(user.getPictureUrl())
                .content(comment.getContent())
                .likes((long) commentsUsersLikeRefs.size())
                .likedUserIds(commentsUsersLikeRefs.stream().map(CommentsUsersLikeRef::getUserId).toList())
                .replies(commentsReplyRefRepository.countByBaseCommentId(comment.getId()))
                .createdAt(comment.getCreatedAt())
                .isEdited(comment.getIsEdited())
                .build();
    }
}
