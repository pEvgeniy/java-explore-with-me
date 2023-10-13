package ru.practicum.repository.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllUsersByIdIn(List<Integer> ids, PageRequest pageRequest);

}
