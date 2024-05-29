package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByConversationIdOrderByTimestampDesc(Long conversationId, Pageable pageable);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE m.conversation.id = ?1 " +
            "AND m.timestamp >= ALL " +
            "(SELECT m1.timestamp FROM ChatMessage m1 " +
            "WHERE m1.conversation.id = ?1)")
    Optional<ChatMessage> findLastChatMessageOfConversation(Long conversationId);
}
