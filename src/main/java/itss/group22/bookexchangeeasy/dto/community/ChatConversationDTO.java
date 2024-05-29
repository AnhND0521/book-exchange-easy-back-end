package itss.group22.bookexchangeeasy.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatConversationDTO {
    private Long id;
    private Long userId1;
    private Long userId2;
    private String userName1;
    private String userName2;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    private Boolean lastSentByUser1;
    private Boolean seenByUser1;
    private Boolean seenByUser2;
}
