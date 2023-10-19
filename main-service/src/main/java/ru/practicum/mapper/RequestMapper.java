package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper extends Mappable<Request, ParticipationRequestDto> {
    @Override
    Request toEntity(ParticipationRequestDto dto);

    @Override
    @Mapping(target = "requester", source = "request.requester.id")
    @Mapping(target = "event", source = "request.event.id")
    ParticipationRequestDto toDto(Request request);
}
