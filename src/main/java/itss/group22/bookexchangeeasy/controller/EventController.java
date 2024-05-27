package itss.group22.bookexchangeeasy.controller;
import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.book.BookDTO;
import itss.group22.bookexchangeeasy.dto.community.EventDTO;
import itss.group22.bookexchangeeasy.dto.common.ResponseMessage;
import itss.group22.bookexchangeeasy.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/events")
    @Operation(
            summary = "Tạo một sự kiện",
            description = "Trường created và concernedUserIds không cần điền, các trường startTime và endTime điền dạng string theo định dạng UTC"
    )
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.createEvent(eventDTO));
    }

    @PutMapping("/events/{id}")
    @Operation(summary = "Cập nhật một sự kiện")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDTO));
    }

    @DeleteMapping("/events/{id}")
    @Operation(summary = "Xóa một sự kiện")
    public ResponseEntity<ResponseMessage> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new ResponseMessage("Event deleted successfully"));
    }

    @GetMapping("/events/latest")
    @Operation(
            summary = "Lấy những sự kiện được đăng gần đây nhất (có phân trang)"
    )
    public ResponseEntity<Page<EventDTO>> getLatestEvents(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(eventService.getLatestEvents(page, size));
    }

    @GetMapping("/events/find-by-owner")
    @Operation(summary = "Lấy danh sách các sự kiện mà một người dùng đã đăng (có phân trang)")
    public ResponseEntity<Page<EventDTO>> getEventsByOwner(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(eventService.getEventsByOwner(userId, page, size));
    }
    @GetMapping("/events/filter-event-that-user-concern")
    @Operation(summary = "Lấy danh sách tất cả các sự kiện mà một người dùng có quan tâm (có phân trang)")
    public ResponseEntity<Page<EventDTO>> getEventsByUserConcern(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(eventService.getEventsByConcernedUser(userId, page, size));
    }

    @GetMapping("/events/{eventId}")
    @Operation(summary = "Lấy thông tin một sự kiện cụ thể")
    public ResponseEntity<EventDTO> getEventDetails(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventDetails(eventId));
    }


}
