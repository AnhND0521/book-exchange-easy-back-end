package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.ChatConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    @Query("SELECT c FROM ChatConversation c " +
            "WHERE c.user1.id = ?1 OR c.user2.id = ?1 " +
            "ORDER BY c.lastMessageTime DESC")
    Page<ChatConversation> findByUser(Long userId, Pageable pageable);

    @Query("SELECT c FROM ChatConversation c " +
            "WHERE (c.user1.id = ?1 AND c.user2.id = ?2) " +
            "OR (c.user1.id = ?2 AND c.user2.id = ?1)")
    Optional<ChatConversation> findByUsers(Long user1Id, Long user2Id);
}
