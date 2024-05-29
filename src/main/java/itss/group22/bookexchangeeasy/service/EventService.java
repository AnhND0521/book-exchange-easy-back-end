package itss.group22.bookexchangeeasy.service;
import itss.group22.bookexchangeeasy.dto.community.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EventService {
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO updateEvent(Long id, EventDTO eventDTO);
    void deleteEvent(Long id);
    Page<EventDTO> getLatestEvents(int page, int size);
    Page<EventDTO> getEventsByOwner(Long userId, int page, int size);
    Page<EventDTO> getEventsByConcernedUser(Long userId, int page, int size);
    EventDTO getEventDetails(Long eventId);
    String uploadEventImage(Long id, MultipartFile imageFile) throws IOException;


}
