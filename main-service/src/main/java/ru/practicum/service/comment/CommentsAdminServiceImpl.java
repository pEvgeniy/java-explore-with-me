package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;
import ru.practicum.repository.CommentRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsAdminServiceImpl implements CommentsAdminService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> findAllComments(String comId, String eventId) {
        return null;
    }

    @Override
    public CommentDto updateComment(String comId, String eventId, UpdateCommentRequestDto updateCommentRequestDto) {
        return null;
    }

    @Override
    public void deleteComment(String comId, String eventId) {

    }
}
