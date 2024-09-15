package itss.group22.bookexchangeeasy.entity;

import itss.group22.bookexchangeeasy.enums.KeyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_key")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String value;

    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    private LocalDateTime createdTime;

    private LocalDateTime expireTime;

    private Boolean isUsed;
}
