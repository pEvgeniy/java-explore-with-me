package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.service.requests.RequestsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestsController {

    private final RequestsService requestsService;

    @GetMapping
    public List<ParticipationRequestDto> findRequestsForUser(@PathVariable Integer userId) {
        log.info("controller. get. /users/{}/requests. trying to find requests for user", userId);
        return requestsService.findRequestsForUser(userId);
    }

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable Integer userId,
                                                 @RequestParam Integer eventId) {
        log.info("controller. post. /users/{}/requests/?eventId={}. trying to find requests for user", userId, eventId);
        return requestsService.createRequestForUser(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipation(@PathVariable Integer userId,
                                                       @PathVariable Integer requestId) {
        log.info("controller. patch. /users/{}/requests/{}/cancel. trying to cancel requests for user", userId, requestId);
        return requestsService.cancelParticipation(userId, requestId);
    }

}
