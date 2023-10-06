package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    EndpointHitDto createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
