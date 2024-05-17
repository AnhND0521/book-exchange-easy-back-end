package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.dto.common.NotificationDTO;
import itss.group22.bookexchangeeasy.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationDTO>> getNotifications(
            @PathVariable Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(notificationService.getNotifications(userId, page, size));
    }
}
