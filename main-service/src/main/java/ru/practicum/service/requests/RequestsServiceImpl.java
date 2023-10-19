package ru.practicum.service.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.dto.requests.RequestStatus;
import ru.practicum.exception.EntityForbiddenException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestsServiceImpl implements RequestsService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findRequestsForUser(Integer userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("service. found requests for user with id = {}, requests = {}", userId, requests);
        return requests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequestForUser(Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with userId = %s not found", userId)));
        validateParticipantsLimit(event.getConfirmedRequests(), event.getParticipantsLimit());
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new EntityForbiddenException("Request already exists");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new EntityForbiddenException("Initiator can not make a request for his own event");
        }
        if (!event.getState().equals(EventFullDto.EventState.PUBLISHED)) {
            throw new EntityForbiddenException("Event have not published yet");
        }
        Request request = requestRepository.save(buildRequest(event, user));
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        log.info("service. created request = {}", request);
        return requestMapper.toDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipation(Integer userId, Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Request with requestId = %s not found", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        log.info("service. changed request status to canceled");
        return requestMapper.toDto(request);
    }

    private void validateParticipantsLimit(Integer confirmedRequests, Integer participantsLimit) {
        if (confirmedRequests >= participantsLimit) {
            throw new EntityForbiddenException("Event palaces are out of limits");
        }
    }

    private Request buildRequest(Event event, User user) {
        return Request.builder()
                .event(event)
                .requester(user)
                .status(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();
    }
}
