package itss.group22.bookexchangeeasy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "store_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime created;

    @ManyToMany
    @JoinTable(
            name = "event_concerned_users",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> concernedUsers;
    private String imagePath;

    @PrePersist
    public void prePersist() {
        if (created == null) created = LocalDateTime.now();
    }
}
