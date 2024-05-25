package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.community.EventDTO;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.PostRepository;
import itss.group22.bookexchangeeasy.repository.StoreEventRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StoreEventRepository eventRepository;
    private final PostRepository postRepository;
    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        StoreEvent event = toEntity(eventDTO);
        event.setId(null);
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
        postRepository.deleteByEventId(event.getId());
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
        if(eventDTO.getConcernedUserIds() != null) {
            event.setConcernedUsers(eventDTO.getConcernedUserIds().stream().map(userId -> userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId))).collect(Collectors.toSet()));
        }

        return event;
    }
}
