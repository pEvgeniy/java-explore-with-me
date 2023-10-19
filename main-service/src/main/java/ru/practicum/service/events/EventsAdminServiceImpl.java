package ru.practicum.service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.UpdateEventAdminRequestDto;
import ru.practicum.dto.events.UpdateEventUserRequestDto;
import ru.practicum.exception.EntityForbiddenException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventFullMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsAdminServiceImpl implements EventsAdminService {

    private final EventRepository eventRepository;

    private final EventFullMapper eventFullMapper;

    private final CategoryMapper categoryMapper;

    private final LocationMapper locationMapper;

    @Override
    public List<EventFullDto> findAllEvents(List<Integer> users,
                                            List<EventFullDto.EventState> states,
                                            List<Integer> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events =
                eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(
                        users, states, categories, rangeStart, rangeEnd, pageRequest);
        log.info("service. found events = {}", events);
        return events.stream()
                .map(eventFullMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        if (event.getState().equals(EventFullDto.EventState.PUBLISHED)
                || LocalDateTime.now().plusHours(2).isBefore(event.getEventDate())) {
            throw new EntityForbiddenException(String.format("Event with eventId = %s forbidden to be changed", eventId));
        }
        updateEventFields(event, updateEventAdminRequestDto);
        return eventFullMapper.toDto(updateEventState(updateEventAdminRequestDto.getStateAction(), event));
    }

    private Event updateEventState(UpdateEventAdminRequestDto.EventUpdateState state, Event event) {
        if (!event.getState().equals(EventFullDto.EventState.PENDING)) {
            throw new EntityForbiddenException(String.format("Unable to cancel or publish event because it's state = %s", event.getState()));
        }
        switch (state) {
            case REJECT_EVENT:
                event.setState(EventFullDto.EventState.CANCELED);
                log.info("service. changed event state to {}", event.getState());
                return event;
            case PUBLISH_EVENT:
                event.setState(EventFullDto.EventState.PUBLISHED);
                log.info("service. changed event state to {}", event.getState());
                return event;
            default:
                log.info("service. status do not need to be ");
                return event;
        }
    }

    private void updateEventFields(Event event, UpdateEventAdminRequestDto updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryMapper.toEntity(updateEvent.getCategory()));
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(locationMapper.toEntity(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantsLimit() != null) {
            event.setParticipantsLimit(updateEvent.getParticipantsLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}
