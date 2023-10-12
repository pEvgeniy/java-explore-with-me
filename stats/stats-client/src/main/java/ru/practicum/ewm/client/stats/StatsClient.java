package ru.practicum.ewm.client.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StatsClient {

    private final WebClient webClient;
    private static final String SERVER_URL = "http://localhost:9090";
    private static final String HIT_ENDPOINT = "/hit";
    private static final String FIND_STATS_ENDPOINT = "/stats";

    public StatsClient() {
        this.webClient = WebClient.create(SERVER_URL);
    }

    public ResponseEntity<EndpointHitDto> createHit(String app, String uri, String ip, LocalDateTime timestamp) {
        return this.webClient.post()
                .uri(HIT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new EndpointHitDto(app, uri, ip, timestamp))
                .retrieve()
                .toEntity(EndpointHitDto.class)
                .doOnSuccess(s -> log.info("web client. successful transition of dto = [app = {}, uri = {}, ip = {}, timestamp = {}]",
                        app, uri, ip, timestamp))
                .doOnError(s -> log.info("web client. error during transition"))
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> findStats(LocalDateTime start,
                                                        LocalDateTime end,
                                                        List<String> uris,
                                                        Boolean unique) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(FIND_STATS_ENDPOINT)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .doOnSuccess(s -> log.info("web client. successful transition of dto = [start = {}, end = {}, uris = {}, unique = {}]",
                        start, end, uris, unique))
                .doOnError(s -> log.info("web client. error during transition"))
                .block();

    }
}
