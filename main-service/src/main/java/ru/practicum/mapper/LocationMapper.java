package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.events.LocationDto;
import ru.practicum.model.Location;

@Mapper
public interface LocationMapper extends Mappable<Location, LocationDto> {

    @Override
    Location toEntity(LocationDto dto);

    @Override
    LocationDto toDto(Location entity);
}
