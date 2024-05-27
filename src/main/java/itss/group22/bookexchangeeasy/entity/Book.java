package itss.group22.bookexchangeeasy.entity;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

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
    private String title;
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
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
    @Enumerated
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime created;
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "books_users_concerned",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> concernedUsers;


    @PrePersist
    public void prePersist() {
        if (created == null) created = LocalDateTime.now();
    }
}
