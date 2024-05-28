package itss.group22.bookexchangeeasy.controller;

import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.common.NotificationDTO;
import itss.group22.bookexchangeeasy.dto.common.ResponseMessage;
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
    @Operation(summary = "Lấy danh sách thông báo của một người dùng")
    public ResponseEntity<Page<NotificationDTO>> getNotifications(
            @PathVariable Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(notificationService.getNotifications(userId, page, size));
    }
    
    @PostMapping("/users/{userId}/notifications/{notificationId}")
    @Operation(summary = "Đánh dấu một thông báo là đã đọc")
    public ResponseEntity<ResponseMessage> markNotificationAsRead(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.markNotificationAsRead(userId, notificationId);
        return ResponseEntity.ok(new ResponseMessage("Notification marked as read"));
    }
}
