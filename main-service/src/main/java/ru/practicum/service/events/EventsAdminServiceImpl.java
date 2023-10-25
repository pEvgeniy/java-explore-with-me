package ru.practicum.service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.UpdateEventAdminRequestDto;
import ru.practicum.exception.EntityConflictException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.event.EventFullMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsAdminServiceImpl implements EventsAdminService {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final EventFullMapper eventFullMapper;

    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAllEvents(List<Integer> users,
                                            List<EventFullDto.EventState> states,
                                            List<Integer> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            Integer from, Integer size) {
        List<Event> events = eventRepository.findAllEventsWithFilters(
                        users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size));
        log.info("service. found events = {}", events);
        return events.stream()
                .map(eventFullMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        if (event.getState().equals(EventFullDto.EventState.PUBLISHED)
                || LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new EntityConflictException(String.format("Event with eventId = %s forbidden to be changed", eventId));
        }
        updateEventFields(event, updateEventAdminRequestDto);
        updateEventState(updateEventAdminRequestDto.getStateAction(), event);
        return eventFullMapper.toDto(event);
    }

    private Event updateEventState(UpdateEventAdminRequestDto.EventUpdateState state, Event event) {
        if (state == null) {
            return event;
        }
        if (!event.getState().equals(EventFullDto.EventState.PENDING)) {
            throw new EntityConflictException(String.format("Unable to cancel or publish event because it's state = %s", event.getState()));
        }
        switch (state) {
            case REJECT_EVENT:
                event.setState(EventFullDto.EventState.CANCELED);
                log.info("service. changed event state to {}", event.getState());
                return event;
            case PUBLISH_EVENT:
                event.setState(EventFullDto.EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                log.info("service. changed event state to {}", event.getState());
                return event;
            default:
                log.info("service. status do not need to be ");
                return event;
        }
    }

    private void updateEventFields(Event event, UpdateEventAdminRequestDto updateEvent) {
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id = %s not found", updateEvent.getCategory())));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            Location location = locationRepository.save(locationMapper.toEntity(updateEvent.getLocation()));
            event.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}
