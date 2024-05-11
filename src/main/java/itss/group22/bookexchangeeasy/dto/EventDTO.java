package itss.group22.bookexchangeeasy.dto;
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
public class EventDTO {
    private Long id;
    private Long ownerId;

    private String name;
    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private LocalDateTime created;
    private Set<Long> concernedUserIds;
}
