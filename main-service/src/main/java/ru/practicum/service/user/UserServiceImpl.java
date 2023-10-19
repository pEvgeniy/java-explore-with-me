package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsersWithParameters(List<Integer> ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<User> foundUsers = userRepository.findAllUsersByIdIn(ids, pageRequest);
        log.info("service. found by params = [ids = {}, from = {}, size = {}] users = {}", ids, from, size, foundUsers);
        return foundUsers.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User createdUser = userRepository.save(userMapper.toEntity(userDto));
        log.info("service. created user = {}", createdUser);
        return userMapper.toDto(createdUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
        log.info("service. created user id = {}", id);
    }
}
