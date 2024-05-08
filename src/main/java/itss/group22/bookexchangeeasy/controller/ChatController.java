package itss.group22.bookexchangeeasy.controller;

import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.ChatConversationDTO;
import itss.group22.bookexchangeeasy.dto.ChatMessageDTO;
import itss.group22.bookexchangeeasy.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/api/v1/users/{userId}/conversations")
    @Operation(summary = "Lấy danh sách các cuộc trò chuyện của một người dùng")
    public ResponseEntity<Page<ChatConversationDTO>> getConversations(
            @PathVariable Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(chatService.getConversations(userId, page, size));
    }

    @GetMapping("/api/v1/users/{userId}/conversations/{conversationId}/messages")
    @Operation(summary = "Lấy các tin nhắn của một cuộc trò chuyện")
    public ResponseEntity<Page<ChatMessageDTO>> getMessagesOfConversation(
            @PathVariable Long userId,
            @PathVariable Long conversationId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(chatService.getMessagesOfConversation(userId, conversationId, page, size));
    }

    @MessageMapping("/chat")
    public void sendChatMessage(ChatMessageDTO chatMessageDTO) {
        chatService.sendChatMessage(chatMessageDTO);
    }
}
