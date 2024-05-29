package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.community.ChatConversationDTO;
import itss.group22.bookexchangeeasy.dto.community.ChatMessageDTO;
import org.springframework.data.domain.Page;

public interface ChatService {
    Page<ChatConversationDTO> getConversations(Long userId, int page, int size);
    ChatConversationDTO findConversationByPartner(Long userId, Long partnerId);
    Page<ChatMessageDTO> getMessagesOfConversation(Long userId, Long conversationId, int page, int size);
    void markConversationAsSeen(Long userId, Long conversationId);
    void sendChatMessage(ChatMessageDTO chatMessageDTO);
}
