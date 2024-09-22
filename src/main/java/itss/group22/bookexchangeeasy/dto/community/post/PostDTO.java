package itss.group22.bookexchangeeasy.dto.community.post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String imagePath;
    private Set<Long> likedUserIds;
    private Long eventId;
    private LocalDateTime created;
    private LocalDateTime lastUpdated;
}
