package itss.group22.bookexchangeeasy.dto.book;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private Long ownerId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    private String publisher;
    private Integer publishYear;
    private String language;
    private String weight;
    private String size;
    private Integer pages;
    private String layout;
    private String description;
    private String imagePath;
    private List<String> categories;
    private String status;
    private LocalDateTime created;
    private Set<Long> concernedUserIds;
}
