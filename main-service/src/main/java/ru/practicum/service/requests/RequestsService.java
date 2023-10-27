package ru.practicum.service.requests;

import ru.practicum.dto.requests.ParticipationRequestDto;

import java.util.List;

public interface RequestsService {

    List<ParticipationRequestDto> findRequestsForUser(Integer userId);

    ParticipationRequestDto createRequestForUser(Integer userId, Integer eventId);

    ParticipationRequestDto cancelParticipation(Integer userId, Integer requestId);

}
