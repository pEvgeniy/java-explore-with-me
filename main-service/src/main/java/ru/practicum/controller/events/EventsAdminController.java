package ru.practicum.controller.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.UpdateEventAdminRequestDto;
import ru.practicum.service.events.EventsAdminService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventsAdminController {

    private final EventsAdminService eventsAdminService;

    @GetMapping
    public List<EventFullDto> findAllEvents(@RequestParam List<Integer> users,
                                            @RequestParam List<EventFullDto.EventState> states,
                                            @RequestParam List<Integer> categories,
                                            @RequestParam LocalDateTime rangeStart,
                                            @RequestParam LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "10") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("controller. get. /admin/events. findAllEvents with parameters = [users = {}, states = {}, categories = {}, " +
                "rangeStart = {}, rangeEnd = {}, from = {}, size = {}]", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventsAdminService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        log.info("controller. patch. /admin/events/{}. update event with update body = {}", eventId, updateEventAdminRequestDto);
        return eventsAdminService.updateEvent(eventId, updateEventAdminRequestDto);
    }

}
