package itss.group22.bookexchangeeasy.service;
import itss.group22.bookexchangeeasy.dto.EventDTO;
public interface EventService {
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO updateEvent(Long id, EventDTO eventDTO);
    void deleteEvent(Long id);
}
