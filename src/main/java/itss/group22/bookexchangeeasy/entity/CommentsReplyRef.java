package itss.group22.bookexchangeeasy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments_reply_ref")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentsReplyRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long baseCommentId;

    private Long replyCommentId;
}
