package ru.practicum.service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.exception.BadParameterException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.event.EventFullMapper;
import ru.practicum.mapper.event.EventShortMapper;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsPublicServiceImpl implements EventsPublicService {

    private final EventRepository eventRepository;

    private final EventShortMapper eventShortMapper;

    private final EventFullMapper eventFullMapper;

    private final StatsClient statsClient;

    @Value("${app.name}")
    private String appName;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findEvents(String text,
                                          List<Integer> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Boolean onlyAvailable,
                                          String sort,
                                          Integer from, Integer size,
                                          HttpServletRequest request) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.MAX;
        }

        List<Event> events = eventRepository.findPublishedEventsWithFilters(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, PageRequest.of(from / size, size));
        sortEvents(sort, events);
        createHit(request);
        return getEventsWithViews(events).stream()
                .map(eventShortMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEventById(Integer id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", id)));
        createHit(request);
        return getEventsWithViews(List.of(event)).stream()
                .findFirst()
                .map(eventFullMapper::toDto)
                .get();
    }

    private List<Event> sortEvents(String sortType, List<Event> events) {
        switch (sortType) {
            case "EVENT_DATE":
                events.sort(Comparator.comparing(Event::getEventDate));
                return events;
            case "VIEWS":
                getEventsWithViews(events).sort(Comparator.comparingInt(Event::getViews));
                return events;
            default:
                throw new BadParameterException(String.format("Event sort type must be EVENT_DATE or VIEWS, but it is %s", sortType));
        }
    }

    private void createHit(HttpServletRequest request) {
        statsClient.createHit(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
    }

    private List<Event> getEventsWithViews(List<Event> events) {
        List<Integer> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        LocalDateTime earliestEvent = eventRepository.findEarliestEvent(ids);

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .collect(Collectors.toList());

        List<ViewStatsDto> viewStats =
                statsClient.findStats(earliestEvent, LocalDateTime.now(), uris, true).getBody();
        if (viewStats != null) {
            Map<Integer, Integer> idsToViews = viewStats.stream()
                    .collect(Collectors.toMap(stats -> parseIdFromUri(stats.getUri()), ViewStatsDto::getHits));

            events.forEach(e -> e.setViews(idsToViews.getOrDefault(e.getId(), 0)));
        }
        return events;
    }

    private Integer parseIdFromUri(String uri) {
        String[] uriParts = uri.split("/");
        return Integer.parseInt(uriParts[uriParts.length - 1]);
    }
}
