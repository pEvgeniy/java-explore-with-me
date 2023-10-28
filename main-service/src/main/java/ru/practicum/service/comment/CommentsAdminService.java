package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;

import java.util.List;

public interface CommentsAdminService {

    List<CommentDto> findAllComments(Integer eventId);

    CommentDto updateComment(Integer comId, Integer eventId, UpdateCommentRequestDto updateCommentRequestDto);

    void deleteComment(Integer comId, Integer eventId);

}
