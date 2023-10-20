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
import ru.practicum.exception.EntityForbiddenException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.event.EventFullMapper;
import ru.practicum.mapper.event.EventNewMapper;
import ru.practicum.mapper.event.EventShortMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsPrivateServiceImpl implements EventsPrivateService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final EventFullMapper eventFullMapper;

    private final EventNewMapper eventNewMapper;

    private final EventShortMapper eventShortMapper;

    private final RequestMapper requestMapper;

    private final CategoryMapper categoryMapper;

    private final LocationMapper locationMapper;

    @Override
    @Transactional
    public EventFullDto createEvent(Integer userId, NewEventDto eventDto) {
        Event event = eventRepository.save(eventNewMapper.toEntity(eventDto));
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
                || LocalDateTime.now().plusHours(2).isBefore(event.getEventDate())) {
            throw new EntityForbiddenException(String.format("Event with eventId = %s forbidden to be changed", eventId));
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
        if ((event.getParticipantsLimit() == 0 || !event.getRequestModeration())
                && requestsToUpdate.getStatus().equals(RequestStatus.CONFIRMED)) {
            return new EventRequestStatusUpdateResultDto(Collections.emptyList(), Collections.emptyList());
        }
        if (event.getParticipantsLimit() == 0 && requestsToUpdate.getStatus().equals(RequestStatus.CANCELED)) {
            return fillEventRequestsWithCanceled(userId, eventId, requestsToUpdate.getRequestIds());
        }
        List<Request> requests = requestRepository.findAllByEventInitiatorIdAndEventIdAndIdIn(userId, eventId,
                requestsToUpdate.getRequestIds());
        return makeEventRequestsFilledWithUpdatedStatuses(requests, event, requestsToUpdate.getStatus());
    }

    private Event updateEvent(Event event, UpdateEventUserRequestDto updateEvent) {
        updateEventFields(event, updateEvent);
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

    private EventRequestStatusUpdateResultDto fillEventRequestsWithCanceled(Integer userId, Integer eventId, List<Integer> requestsIds) {
        return new EventRequestStatusUpdateResultDto(
                Collections.emptyList(),
                requestRepository.findAllByEventInitiatorIdAndEventIdAndIdIn(userId, eventId, requestsIds)
                        .stream()
                        .map(requestMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    private EventRequestStatusUpdateResultDto makeEventRequestsFilledWithUpdatedStatuses(List<Request> requests,
                                                                                         Event event,
                                                                                         RequestStatus requestStatus) {
        EventRequestStatusUpdateResultDto updateResultDto = new EventRequestStatusUpdateResultDto();
        for (Request request : requests) {
            if (requestStatus.equals(RequestStatus.CONFIRMED)) {
                if (event.getConfirmedRequests() < event.getParticipantsLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    updateResultDto.getConfirmedRequests().add(requestMapper.toDto(request));
                } else {
                    request.setStatus(RequestStatus.CANCELED);
                    updateResultDto.getRejectedRequests().add(requestMapper.toDto(request));
                }
            }
            request.setStatus(RequestStatus.CANCELED);
            updateResultDto.getRejectedRequests().add(requestMapper.toDto(request));
        }
        return updateResultDto;
    }
}
