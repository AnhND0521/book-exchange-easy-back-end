package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.common.NotificationDTO;
import itss.group22.bookexchangeeasy.entity.Notification;
import itss.group22.bookexchangeeasy.repository.NotificationRepository;
import itss.group22.bookexchangeeasy.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ModelMapper mapper;

    @Override
    public Page<NotificationDTO> getNotifications(Long userId, int page, int size) {
        return notificationRepository.findByUserId(userId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    @Override
    public void sendNotification(Notification notification) {
        notification.setTimestamp(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        log.info("Saved notification: " + notification);

        var dto = toDTO(notification);
        messagingTemplate.convertAndSend("/user/" + notification.getUser().getId() + "/notification", dto);
        log.info("Sent notification: " + dto);
    }

    private NotificationDTO toDTO(Notification notification) {
        var dto = mapper.map(notification, NotificationDTO.class);
        dto.setUserId(notification.getUser().getId());
        dto.setType(notification.getType().toString());
        return dto;
    }
}
