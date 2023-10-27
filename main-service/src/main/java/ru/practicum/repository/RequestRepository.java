package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequesterId(Integer requesterId);

    Boolean existsByRequesterIdAndEventId(Integer requesterId, Integer eventId);

    List<Request> findAllByEventInitiatorIdAndEventId(Integer initiatorId, Integer eventId);

    List<Request> findAllByEventInitiatorIdAndEventIdAndIdIn(Integer initiatorId, Integer eventId, List<Integer> requests);


}
