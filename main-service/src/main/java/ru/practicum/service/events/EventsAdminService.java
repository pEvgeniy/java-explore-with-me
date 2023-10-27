package ru.practicum.service.events;

import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.UpdateEventAdminRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsAdminService {

    List<EventFullDto> findAllEvents(List<Integer> users,
                                     List<EventFullDto.EventState> states,
                                     List<Integer> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     Integer from, Integer size);

    EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

}
