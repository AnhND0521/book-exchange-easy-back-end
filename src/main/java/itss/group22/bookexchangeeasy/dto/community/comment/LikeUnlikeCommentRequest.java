package itss.group22.bookexchangeeasy.dto.community.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeUnlikeCommentRequest {
    @NotNull
    private Long userId;
}
