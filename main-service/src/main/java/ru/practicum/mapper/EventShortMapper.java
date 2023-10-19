package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.model.Event;

@Mapper(componentModel = "spring")
public interface EventShortMapper extends Mappable<Event, EventShortDto> {
    @Override
    EventShortDto toDto(Event entity);
}
