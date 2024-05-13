package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.EventDTO;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.StoreEventRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StoreEventRepository eventRepository;
    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        StoreEvent event = toEntity(eventDTO);
        event.setId(null);
        event.setConcernedUsers(null);
        event = eventRepository.save(event);
        return toDTO(event);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        StoreEvent oldEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
       StoreEvent event = toEntity(eventDTO);
        event.setConcernedUsers(oldEvent.getConcernedUsers());
        event.setCreated(oldEvent.getCreated());
        event.setOwner(oldEvent.getOwner());
        event.setId(id);
        event = eventRepository.save(event);

        return toDTO(event);
    }

    @Override
    public void deleteEvent(Long id) {
        StoreEvent event  = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        eventRepository.delete(event);
    }


    private EventDTO toDTO(StoreEvent event) {
        EventDTO eventDTO = mapper.map(event, EventDTO.class);
        eventDTO.setOwnerId(event.getOwner().getId());
        eventDTO.setConcernedUserIds(event.getConcernedUsers().stream().map(user -> user.getId()).collect(Collectors.toSet()));

        return eventDTO;
    }
    private StoreEvent toEntity(EventDTO eventDTO) {
        StoreEvent event = mapper.map(eventDTO, StoreEvent.class);
        event.setOwner(userRepository.findById(eventDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", eventDTO.getOwnerId())));
        event.setConcernedUsers(eventDTO.getConcernedUserIds().stream().map(userId -> userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId))).collect(Collectors.toSet()));
        return event;
    }
}