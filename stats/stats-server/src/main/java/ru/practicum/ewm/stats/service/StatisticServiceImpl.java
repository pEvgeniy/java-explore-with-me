package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.EndpointHit;
import ru.practicum.ewm.stats.StatisticRepository;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    private final EndpointHitMapper endpointHitMapper;

    @Override
    @Transactional
    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        log.info("service. create hit = {}", endpointHitDto);
        EndpointHit endpointStatistic = endpointHitMapper.toEndpointHit(endpointHitDto);
        statisticRepository.save(endpointStatistic);
        return endpointHitMapper.toEndpointHitDto(endpointStatistic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("service. find statistic with params = [start = {}, end = {}, uris = {}, unique = {}]",
                start, end, uris, unique);
        validateDates(start, end);
        if (uris.isEmpty()) {
            return statisticRepository.findStatisticsByParametersWithoutUriFilter(start, end, unique);
        }
        return statisticRepository.findStatisticsByParametersWithUriFilter(start, end, uris, unique);
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException(String.format("LocalDateTime validation error. Start must be before end. Start = %s, End = %s",
                    start, end));
        }
    }
}
