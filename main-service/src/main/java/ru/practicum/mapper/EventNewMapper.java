package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.model.Event;

@Mapper(componentModel = "spring")
public interface EventNewMapper extends Mappable<Event, NewEventDto> {

    @Override
    Event toEntity(NewEventDto dto);

    @Override
    NewEventDto toDto(Event entity);
}
