package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;

public interface CommentsPrivateService {

    CommentDto createComment(Integer userId, Integer eventId, CommentDto commentDto);

    CommentDto updateComment(Integer userId, Integer eventId, Integer comId, UpdateCommentRequestDto updateCommentRequestDto);

    void deleteComment(Integer userId, Integer eventId, Integer comId);

}
