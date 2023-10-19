package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.model.Event;

@Mapper(componentModel = "spring")
public interface EventFullMapper extends Mappable<Event, EventFullDto> {

    @Override
    Event toEntity(EventFullDto dto);

    @Override
    EventFullDto toDto(Event entity);
}
