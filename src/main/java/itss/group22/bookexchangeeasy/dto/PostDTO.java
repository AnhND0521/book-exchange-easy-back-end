package itss.group22.bookexchangeeasy.dto;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.Image;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import itss.group22.bookexchangeeasy.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Boolean isBookPost;
    private BookDTO book;
    private List<ImageDTO> images;
    private Set<Long> likedUserIds;
    private Long eventId;
    private LocalDateTime created;
    private LocalDateTime lastUpdated;
}
