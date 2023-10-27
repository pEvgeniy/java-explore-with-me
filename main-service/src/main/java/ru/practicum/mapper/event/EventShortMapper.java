package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.mapper.Mappable;
import ru.practicum.model.Event;

@Mapper(componentModel = "spring")
public interface EventShortMapper extends Mappable<Event, EventShortDto> {
    @Override
    EventShortDto toDto(Event entity);
}
