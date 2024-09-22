package itss.group22.bookexchangeeasy.dto.community.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userPictureUrl;
    private Long eventId;
    private String eventName;
    private String title;
    private String content;
    private String imagePath;
    private Set<Long> likedUserIds;
    private Long commentCount;
    private LocalDateTime created;
}
