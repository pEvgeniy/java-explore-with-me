package ru.practicum.service.user;

import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsersWithParameters(List<Integer> ids, Integer from, Integer size);

    UserDto createUser(UserDto userDto);

    void deleteUserById(Integer id);

}
