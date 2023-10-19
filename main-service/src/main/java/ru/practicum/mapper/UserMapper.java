package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.UserDto;
import ru.practicum.mapper.Mappable;
import ru.practicum.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

    @Override
    User toEntity(UserDto dto);

    @Override
    UserDto toDto(User entity);

}
