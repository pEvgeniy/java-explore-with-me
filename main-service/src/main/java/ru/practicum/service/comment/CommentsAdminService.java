package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;

import java.util.List;

public interface CommentsAdminService {

    List<CommentDto> findAllComments(String comId, String eventId);

    CommentDto updateComment(String comId, String eventId, UpdateCommentRequestDto updateCommentRequestDto);

    void deleteComment(String comId, String eventId);

}
