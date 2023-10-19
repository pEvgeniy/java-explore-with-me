package ru.practicum.controller.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.service.events.EventsPublicService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsPublicController {

    private final EventsPublicService eventsPublicService;

    @GetMapping
    public List<EventShortDto> findEvents(@RequestParam String text,
                                          @RequestParam List<Integer> categories,
                                          @RequestParam Boolean paid,
                                          @RequestParam LocalDateTime rangeStart,
                                          @RequestParam LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam SortVariation sort,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          HttpServletRequest request) {
        log.info("controller. get. /events. find events with parameters = [text = {}, categories = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}]",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventsPublicService.findEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto findEventById(@PathVariable Integer id, HttpServletRequest request) {
        log.info("controller. get. /events/{}. find event", id);
        return eventsPublicService.findEventById(id, request);
    }

    public enum SortVariation {
        EVENT_DATE, VIEWS
    }

}
