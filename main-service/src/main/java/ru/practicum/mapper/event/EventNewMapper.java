package ru.practicum.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.mapper.Mappable;
import ru.practicum.model.Event;

@Mapper(componentModel = "spring")
public interface EventNewMapper extends Mappable<Event, NewEventDto> {

    @Override
    @Mapping(target = "category.id", source = "category")
    Event toEntity(NewEventDto dto);

    @Override
    @Mapping(target = "category", source = "category.id")
    NewEventDto toDto(Event entity);
}
