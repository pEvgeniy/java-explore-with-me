package ru.practicum.service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.dto.events.UpdateEventUserRequestDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateResultDto;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.dto.requests.RequestStatus;
import ru.practicum.exception.EntityConflictException;
import ru.practicum.exception.EntityForbiddenException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.event.EventFullMapper;
import ru.practicum.mapper.event.EventNewMapper;
import ru.practicum.mapper.event.EventShortMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.dto.requests.EventRequestStatusUpdateRequestDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsPrivateServiceImpl implements EventsPrivateService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final EventFullMapper eventFullMapper;

    private final EventNewMapper eventNewMapper;

    private final EventShortMapper eventShortMapper;

    private final RequestMapper requestMapper;

    private final LocationMapper locationMapper;

    @Override
    @Transactional
    public EventFullDto createEvent(Integer userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id = %s not found", userId)));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id = %s not found", eventDto.getCategory())));
        Event event = eventNewMapper.toEntity(eventDto);
        setEventFields(event, user, category, event.getLocation());
        eventRepository.save(event);
        log.info("service. created event = {}", event);
        return eventFullMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findEventsForUser(Integer userId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);
        log.info("service. found events for user with id = {}, events = {}", userId, events);
        return events.stream()
                .map(eventShortMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEventForUser(Integer userId, Integer eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s and userId = %s not found", eventId, userId)));
        log.info("service. found event with eventId = {} and userId = {}", eventId, userId);
        return eventFullMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventForUser(Integer userId, Integer eventId, UpdateEventUserRequestDto eventDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s and userId = %s not found", eventId, userId)));
        if (event.getState().equals(EventFullDto.EventState.PUBLISHED)
                || LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new EntityConflictException(String.format("Event with eventId = %s forbidden to be changed", eventId));
        }
        return eventFullMapper.toDto(updateEvent(event, eventDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findRequestsForUser(Integer userId, Integer eventId) {
        List<Request> requests = requestRepository.findAllByEventInitiatorIdAndEventId(userId, eventId);
        log.info("service. found requests for initiator with id = {} and event with id = {}, requests = {}",
                userId, eventId, requests);
        return requests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto updateRequestsForEvents(Integer userId, Integer eventId,
                                                                     EventRequestStatusUpdateRequestDto requestsToUpdate) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        if ((event.getParticipantLimit() == 0 || !event.getRequestModeration())
                && requestsToUpdate.getStatus().equals(RequestUpdateStatus.REJECTED)) {
            return new EventRequestStatusUpdateResultDto(Collections.emptyList(), Collections.emptyList());
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new EntityConflictException("Participant limit is full");
        }
        List<Request> requests = requestRepository.findAllByEventInitiatorIdAndEventIdAndIdIn(userId, eventId,
                requestsToUpdate.getRequestIds());
        return makeEventRequestsFilledWithUpdatedStatuses(requests, event, requestsToUpdate.getStatus());
    }

    private void setEventFields(Event event, User user, Category category, Location location) {
        locationRepository.save(location);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);
        event.setState(EventFullDto.EventState.PENDING);
    }

    private Event updateEvent(Event event, UpdateEventUserRequestDto updateEvent) {
        updateEventFields(event, updateEvent);
        if (updateEvent.getStateAction() == null) {
            return event;
        }
        switch (updateEvent.getStateAction()) {
            case CANCEL_REVIEW:
                event.setState(EventFullDto.EventState.CANCELED);
                log.info("service. changed event state to {}", event.getState());
                return event;
            case SEND_TO_REVIEW:
                event.setState(EventFullDto.EventState.PENDING);
                log.info("service. changed event state to {}", event.getState());
                return event;
            default:
                log.info("service. status do not need to be ");
                return event;
        }
    }

    private void updateEventFields(Event event, UpdateEventUserRequestDto updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id = %s not found", updateEvent.getCategory())));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
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
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }

    private EventRequestStatusUpdateResultDto makeEventRequestsFilledWithUpdatedStatuses(List<Request> requests,
                                                                                         Event event,
                                                                                         RequestUpdateStatus requestStatus) {
        EventRequestStatusUpdateResultDto updateResultDto = new EventRequestStatusUpdateResultDto();
        for (Request request : requests) {
            if (requestStatus.equals(RequestUpdateStatus.CONFIRMED)) {
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    updateResultDto.getConfirmedRequests().add(requestMapper.toDto(request));
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    updateResultDto.getRejectedRequests().add(requestMapper.toDto(request));
                }
            } else {
                request.setStatus(RequestStatus.REJECTED);
                updateResultDto.getRejectedRequests().add(requestMapper.toDto(request));
            }
        }
        return updateResultDto;
    }
}
