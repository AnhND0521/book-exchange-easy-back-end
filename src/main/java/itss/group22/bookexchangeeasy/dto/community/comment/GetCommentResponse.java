package itss.group22.bookexchangeeasy.dto.community.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCommentResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userPictureUrl;
    private String content;
    private Long likes;
    private Long replies;
}
