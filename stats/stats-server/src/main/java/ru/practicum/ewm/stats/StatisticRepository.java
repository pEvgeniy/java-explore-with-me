package ru.practicum.ewm.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("SELECT NEW ru.practicum.ewm.dto.stats.ViewStatsDto(e.app, e.uri," +
            " CASE WHEN :unique = true THEN 1 ELSE COUNT(e) END)" +
            " FROM EndpointHit e" +
            " WHERE e.createdAt BETWEEN :start AND :end" +
            " AND e.uri IN :uris" +
            " AND (:unique = false OR e.ip IN (SELECT DISTINCT e2.ip FROM EndpointHit e2))" +
            " GROUP BY e.app, e.uri" +
            " ORDER BY CASE WHEN :unique = true THEN 1 ELSE COUNT(e) END DESC")
    List<ViewStatsDto> findStatisticsByParametersWithUriFilter(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris,
            @Param("unique") Boolean unique
    );

    @Query("SELECT NEW ru.practicum.ewm.dto.stats.ViewStatsDto(e.app, e.uri," +
            " CASE WHEN :unique = true THEN 1 ELSE COUNT(e) END)" +
            " FROM EndpointHit e" +
            " WHERE e.createdAt BETWEEN :start AND :end" +
            " AND (:unique = false OR e.ip IN (SELECT DISTINCT e2.ip FROM EndpointHit e2))" +
            " GROUP BY e.app, e.uri" +
            " ORDER BY CASE WHEN :unique = true THEN 1 ELSE COUNT(e) END DESC")
    List<ViewStatsDto> findStatisticsByParametersWithoutUriFilter(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("unique") Boolean unique
    );

}
