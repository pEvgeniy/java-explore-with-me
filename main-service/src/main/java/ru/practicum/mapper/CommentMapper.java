package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper extends Mappable<Comment, CommentDto> {
    @Override
    Comment toEntity(CommentDto dto);

    @Override
    CommentDto toDto(Comment entity);
}
