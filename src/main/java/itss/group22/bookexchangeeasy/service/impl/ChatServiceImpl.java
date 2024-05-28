package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.community.ChatConversationDTO;
import itss.group22.bookexchangeeasy.dto.community.ChatMessageDTO;
import itss.group22.bookexchangeeasy.entity.ChatConversation;
import itss.group22.bookexchangeeasy.entity.ChatMessage;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.MessageType;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.ChatConversationRepository;
import itss.group22.bookexchangeeasy.repository.ChatMessageRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.ChatService;
import itss.group22.bookexchangeeasy.service.datastructure.OnlineUserSet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUserSet onlineUserSet;
    private final ModelMapper mapper;

    @Override
    public Page<ChatConversationDTO> getConversations(Long userId, int page, int size) {
        return conversationRepository.findByUser(userId, PageRequest.of(page, size)).map(this::toDTO);
    }

    @Override
    public ChatConversationDTO findConversationByPartner(Long userId, Long partnerId) {
        var conversation = conversationRepository.findByUsers(userId, partnerId)
                .orElseThrow(() -> new ApiException(
                        "Chat conversation not found between user '%d' and user '%d'".formatted(userId, partnerId),
                        HttpStatus.NOT_FOUND
                ));
        return toDTO(conversation);
    }

    @Override
    public Page<ChatMessageDTO> getMessagesOfConversation(Long userId, Long conversationId, int page, int size) {
        var conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat conversation", "id", conversationId));

        if (!conversation.getUser1().getId().equals(userId) && !conversation.getUser2().getId().equals(userId))
            throw new ApiException("Conversation does not belong to this user");

        return messageRepository.findByConversationIdOrderByTimestampDesc(conversationId, PageRequest.of(page, size)).map(this::toDTO);
    }

    @Override
    public void markConversationAsSeen(Long userId, Long conversationId) {
        var conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat conversation", "id", conversationId));

        if (!conversation.getUser1().getId().equals(userId) && !conversation.getUser2().getId().equals(userId))
            throw new ApiException("Conversation does not belong to this user");

        if (conversation.getUser1().getId().equals(userId)) {
            conversation.setSeenByUser1(true);
        } else {
            conversation.setSeenByUser2(true);
        }
        conversationRepository.save(conversation);
    }

    @Override
    public void sendChatMessage(ChatMessageDTO chatMessageDTO) {
        // get sender and receiver
        User sender = userRepository.findById(chatMessageDTO.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", chatMessageDTO.getSenderId()));
        User receiver = userRepository.findById(chatMessageDTO.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", chatMessageDTO.getReceiverId()));
        ChatConversation conversation = null;

        // get conversation
        if (chatMessageDTO.getConversationId() != null) {
            conversation = conversationRepository.findById(chatMessageDTO.getConversationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", chatMessageDTO.getConversationId()));
        } else {
            var findResult = conversationRepository.findByUsers(sender.getId(), receiver.getId());
            if (findResult.isPresent()) {
                conversation = findResult.get();
            } else {
                conversation = conversationRepository.save(ChatConversation.builder()
                        .user1(sender)
                        .user2(receiver)
                        .build());
            }
        }

        // save message
        ChatMessage message = ChatMessage.builder()
                .conversation(conversation)
                .sender(sender)
                .receiver(receiver)
                .messageType(MessageType.valueOf(chatMessageDTO.getMessageType()))
                .content(chatMessageDTO.getContent())
                .build();
        message = messageRepository.save(message);

        // update conversation timestamp
        conversation.setLastMessageTime(message.getTimestamp());
        if (message.getSender().getId().equals(conversation.getUser1().getId())) {
            conversation.setSeenByUser1(true);
            conversation.setSeenByUser2(false);
        } else {
            conversation.setSeenByUser1(false);
            conversation.setSeenByUser2(true);
        }
        conversationRepository.save(conversation);

        // forward message through websocket
        var dto = toDTO(message);
        if (onlineUserSet.containsKey(sender.getId()))
            messagingTemplate.convertAndSend("/user/" + sender.getId() + "/chat", dto);
        if (onlineUserSet.containsKey(receiver.getId()))
            messagingTemplate.convertAndSend("/user/" + receiver.getId() + "/chat", dto);
    }

    private ChatConversationDTO toDTO(ChatConversation conversation) {
        var dto = mapper.map(conversation, ChatConversationDTO.class);
        dto.setUserId1(conversation.getUser1().getId());
        dto.setUserId2(conversation.getUser2().getId());
        dto.setUserName1(conversation.getUser1().getName());
        dto.setUserName2(conversation.getUser2().getName());
        var lastMessage = messageRepository.findLastChatMessageOfConversation(conversation.getId());
        lastMessage.ifPresent(message -> {
            dto.setLastMessageContent(message.getContent());
            dto.setLastSentByUser1(message.getSender().getId().equals(conversation.getUser1().getId()));
        });
        return dto;
    }

    private ChatMessageDTO toDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .receiverId(message.getReceiver().getId())
                .receiverName(message.getReceiver().getName())
                .messageType(message.getMessageType().name())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}
