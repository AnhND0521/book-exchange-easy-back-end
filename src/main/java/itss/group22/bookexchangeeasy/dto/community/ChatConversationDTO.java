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
    private Long user1_id;
    private String user1_name;
    private Long user2_id;
    private String user2_name;
    private LocalDateTime lastMessageTime;
}
