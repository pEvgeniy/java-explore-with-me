package ru.practicum.service.events;

import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventsPublicService {

    List<EventShortDto> findEvents(String text,
                                   List<Integer> categories,
                                   Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable,
                                   String sort,
                                   Integer from, Integer size,
                                   HttpServletRequest request);

    EventFullDto findEventById(Integer id, HttpServletRequest request);

}
