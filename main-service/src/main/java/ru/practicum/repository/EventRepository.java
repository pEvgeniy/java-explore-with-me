package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.events.EventFullDto.EventState;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    @Query("SELECT MIN(e.publishedOn) " +
            "FROM Event e " +
            "WHERE e.id IN :ids")
    Optional<LocalDateTime> findEarliestEvent(@Param("ids") List<Integer> ids);

    List<Event> findAllByInitiatorId(Integer initiatorId, PageRequest pageRequest);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Integer initiatorId);

    Boolean existsEventByCategoryId(Integer catId);

    @Query("SELECT e FROM Event e " +
            "WHERE (:userIds IS NULL OR e.initiator.id IN :userIds) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categoryIds IS NULL OR e.category.id IN :categoryIds) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR e.eventDate > :rangeStart) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR e.eventDate < :rangeEnd)")
    List<Event> findAllEventsWithFilters(
            @Param("userIds") List<Integer> userIds,
            @Param("states") List<EventState> states,
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageRequest
    );

    @Query("SELECT e FROM " +
            "Event as e " +
            "WHERE ((:text IS NULL) OR " +
            "(LOWER(e.annotation) LIKE %:text%) OR " +
            "(LOWER(e.description) LIKE %:text%)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (e.eventDate IS NULL OR e.eventDate IS NOT NULL) " +
            "AND (false = :onlyAvailable OR (true = :onlyAvailable AND e.participantLimit > 0)) " +
            "AND (COALESCE(:start, NULL) IS NULL OR e.eventDate > :start) " +
            "AND (COALESCE(:end, NULL) IS NULL OR e.eventDate < :end)")
    List<Event> findPublishedEventsWithFilters(
            @Param("text") String text,
            @Param("categories") Collection<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("onlyAvailable") Boolean onlyAvailable,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("pageable") Pageable pageable
    );

}
