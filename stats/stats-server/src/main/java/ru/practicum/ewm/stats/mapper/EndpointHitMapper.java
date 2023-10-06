package ru.practicum.ewm.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.stats.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    @Mapping(source = "timestamp", target = "createdAt")
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

    @Mapping(source = "createdAt", target = "timestamp")
    EndpointHitDto toEndpointHitDto(EndpointHit endpointStatistic);

}