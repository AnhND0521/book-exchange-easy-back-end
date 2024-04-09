package itss.group22.bookexchangeeasy.dto;

import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    @NotEmpty
    private String name;
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
    private String status;
    private LocalDateTime created;
}
