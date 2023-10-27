package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsPrivateServiceImpl implements CommentsPrivateService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentDto createComment(Integer userId, Integer eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id = %s not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s and userId = %s not found", eventId, userId)));
        Comment comment = commentRepository.save(commentMapper.toEntity(commentDto));
        log.info("service. create comment with body = {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto updateComment(Integer userId, Integer eventId, Integer comId, UpdateCommentRequestDto updateCommentRequestDto) {
        return null;
    }

    @Override
    public void deleteComment(Integer userId, Integer eventId, Integer comId) {

    }
}
