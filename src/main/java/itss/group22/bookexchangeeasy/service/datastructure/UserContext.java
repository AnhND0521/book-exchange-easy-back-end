package itss.group22.bookexchangeeasy.service.datastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContext {
    private Long userId;
    private String sessionId;
}
