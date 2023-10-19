package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.controller.events.EventsPublicController;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT MIN(e.publishedAt) " +
            "FROM Event e " +
            "WHERE e.id IN :ids")
    LocalDateTime findEarliestEvent(List<Integer> ids);

    List<Event> findAllByInitiatorId(Integer initiatorId, PageRequest pageRequest);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Integer initiatorId);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(List<Integer> users,
                                                                                   List<EventFullDto.EventState> states,
                                                                                   List<Integer> categories,
                                                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                   PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR " +
            "       LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.category.id IN :categories " +
            "AND e.paid = :paid " +
            "AND  e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND " +
            "CASE WHEN :onlyAvailable = true THEN (e.confirmedRequests < e.participantsLimit) END " +
//            "AND e.confirmedRequests < e.participantsLimit OR e.participantsLimit IS NULL " +
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END")
    List<Event> findPublishedEventsWithFilters(String text,
                                               List<Integer> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               EventsPublicController.SortVariation sort,
                                               PageRequest pageRequest);

}
