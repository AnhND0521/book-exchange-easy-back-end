package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.NotificationDTO;
import itss.group22.bookexchangeeasy.entity.Notification;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<NotificationDTO> getNotifications(Long userId, int page, int size);
    void sendNotification(Notification notification);
}
