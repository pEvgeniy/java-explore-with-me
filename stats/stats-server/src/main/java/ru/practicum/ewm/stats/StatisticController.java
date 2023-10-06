package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.dto.stats.EndpointHitDto.TimeFormatter.DATE_TIME_PATTERN;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("controller. post. /hit. create hit. body = {}", endpointHitDto);
        return statisticService.createHit(endpointHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> findStats(@RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                                        @RequestParam(defaultValue = "") List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("controller. post. /stats. find statistic. params = [start = {}, end = {}, uris = {}, unique = {}]",
                start, end, uris, unique);
        return statisticService.findStats(start, end, uris, unique);
    }

}
