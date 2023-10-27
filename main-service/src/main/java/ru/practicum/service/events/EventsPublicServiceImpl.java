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
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.dto.stats.EndpointHitDto.TimeFormatter.DATE_TIME_PATTERN;

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
        validateDates(rangeStart, rangeEnd);

        List<Event> events = eventRepository.findPublishedEventsWithFilters(
                        text == null ? null : text.toLowerCase(), categories, paid, onlyAvailable,
                        rangeStart, rangeEnd, PageRequest.of(from / size, size));
        if (!events.isEmpty()) {
            sortEvents(sort, events);
            createHit(request);
        }
        return getEventsWithViews(events).stream()
                .map(eventShortMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findEventById(Integer id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", id)));
        if (!event.getState().equals(EventFullDto.EventState.PUBLISHED)) {
            throw new EntityNotFoundException(String.format("Event with eventId = %s not found", id));
        }
        createHit(request);
        return getEventsWithViews(List.of(event)).stream()
                .findFirst()
                .map(eventFullMapper::toDto)
                .get();
    }

    private void sortEvents(String sortType, List<Event> events) {
        switch (sortType) {
            case "EVENT_DATE":
                events.sort(Comparator.comparing(Event::getEventDate));
                return;
            case "VIEWS":
                getEventsWithViews(events).sort(Comparator.comparingInt(Event::getViews));
                return;
            default:
                throw new BadParameterException(String.format("Event sort type must be EVENT_DATE or VIEWS, but it is %s", sortType));
        }
    }

    private void createHit(HttpServletRequest request) {
        statsClient.createHit(appName, request.getRequestURI(), request.getRemoteAddr(),
                formatLocalDateTime(LocalDateTime.now()));
    }

    private List<Event> getEventsWithViews(List<Event> events) {
        if (events.isEmpty()) {
            return events;
        }
        List<Integer> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Optional<LocalDateTime> earliestEvent = eventRepository.findEarliestEvent(ids);

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .collect(Collectors.toList());

        List<ViewStatsDto> viewStats =
                statsClient.findStats(
                        formatLocalDateTime(earliestEvent.orElseGet(() -> LocalDateTime.now().minusYears(30))),
                        formatLocalDateTime(LocalDateTime.now()), uris, true).getBody();
        if (viewStats != null) {
            Map<Integer, Integer> idsToViews = viewStats.stream()
                    .collect(Collectors.toMap(stats -> parseIdFromUri(stats.getUri()), ViewStatsDto::getHits));

            events.forEach(e -> e.setViews(idsToViews.getOrDefault(e.getId(), 0)));
        }
        return events;
    }

    private void validateDates(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }
        if (rangeEnd.isBefore(rangeStart)) {
            throw new IllegalArgumentException(
                    String.format("Range start must be before range end. (rangeStart = %s, rangeEnd = %s)",
                            rangeStart, rangeEnd));
        }
    }

    private Integer parseIdFromUri(String uri) {
        String[] uriParts = uri.split("/");
        return Integer.parseInt(uriParts[uriParts.length - 1]);
    }

    private LocalDateTime formatLocalDateTime(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }
}
