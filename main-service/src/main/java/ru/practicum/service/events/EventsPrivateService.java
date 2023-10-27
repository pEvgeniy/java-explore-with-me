package ru.practicum.service.events;

import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.dto.events.UpdateEventUserRequestDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateResultDto;

import java.util.List;

public interface EventsPrivateService {

    EventFullDto createEvent(Integer userId, NewEventDto eventDto);

    List<EventShortDto> findEventsForUser(Integer userId, Integer from, Integer size);

    EventFullDto findEventForUser(Integer userId, Integer eventId);

    EventFullDto updateEventForUser(Integer userId, Integer eventId, UpdateEventUserRequestDto eventDto);

    List<ParticipationRequestDto> findRequestsForUser(Integer userId, Integer eventId);

    EventRequestStatusUpdateResultDto updateRequestsForEvents(Integer userId, Integer eventId,
                                                              EventRequestStatusUpdateRequestDto requestDto);

}
