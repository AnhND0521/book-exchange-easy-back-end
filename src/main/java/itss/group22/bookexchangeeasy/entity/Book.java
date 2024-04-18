package itss.group22.bookexchangeeasy.entity;

import itss.group22.bookexchangeeasy.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private String publisher;
    private Integer publishYear;
    private String language;
    private String weight;
    private String size;
    private Integer pages;
    private String layout;

    @Column(name = "description", length = 1000)
    private String description;
    private String imagePath;

    @Enumerated
    private BookStatus status;

//    @OneToOne
//    @JoinColumn(name = "post_id")
//    private Post post;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @CreationTimestamp
    private LocalDateTime created;
}
