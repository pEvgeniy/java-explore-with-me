package ru.practicum.controller.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.dto.events.UpdateEventUserRequestDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.requests.EventRequestStatusUpdateResultDto;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.service.events.EventsPrivateService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventsPrivateController {

    private final EventsPrivateService eventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Integer userId, @RequestBody @Valid NewEventDto eventDto) {
        log.info("controller. post. /users/{userId}/events. create event with userId = {} and body = {}",
                userId, eventDto);
        return eventsService.createEvent(userId, eventDto);
    }

    @GetMapping
    public List<EventShortDto> findEventsForUser(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("controller. get. /users/{}/events. find events with parameters = [from = {}, size = {}]",
                userId, from, size);
        return eventsService.findEventsForUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventForUser(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("controller. get. /users/{}/events/{}. find event", userId, eventId);
        return eventsService.findEventForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventForUser(@PathVariable Integer userId, @PathVariable Integer eventId,
                                           @RequestBody @Valid UpdateEventUserRequestDto requestDto) {
        log.info("controller. patch. /users/{}/events/{}. update event", userId, eventId);
        return eventsService.updateEventForUser(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsForUser(@PathVariable Integer userId,
                                                             @PathVariable Integer eventId) {
        log.info("controller. get. /users/{}/events/{}/requests. find requests", userId, eventId);
        return eventsService.findRequestsForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateRequestsForEvents(@PathVariable Integer userId,
                                                                     @PathVariable Integer eventId,
                                                                     @RequestBody @Valid EventRequestStatusUpdateRequestDto
                                                                                 requestsToUpdate) {
        log.info("controller. patch. /users/{}/events/{}/requests. patch requests", userId, eventId);
        return eventsService.updateRequestsForEvents(userId, eventId, requestsToUpdate);
    }

}
