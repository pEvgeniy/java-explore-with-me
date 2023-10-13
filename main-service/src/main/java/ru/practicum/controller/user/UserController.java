package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAllUsersWithParameters(@RequestParam List<Integer> ids,
                                                    @RequestParam Integer from,
                                                    @RequestParam Integer size) {
        log.info("controller. get. /admin/users. find all suers with parameters = [ids = {}, from = {}, size = {}]",
                ids, from, size);
        return userService.findAllUsersWithParameters(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("controller. post. /admin/users. create user with body = {}", userDto);
        return userService.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        log.info("controller. post. /admin/users. delete user with id = {}", id);
        userService.deleteUserById(id);
    }

}
