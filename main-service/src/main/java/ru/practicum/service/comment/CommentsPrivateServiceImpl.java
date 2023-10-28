package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;
import ru.practicum.exception.EntityConflictException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.Objects;

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
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setEvent(event);
        comment.setCommentOwner(user);
        event.getComments().add(comment);
        commentRepository.save(comment);
        log.info("service. create comment with body = {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto updateComment(Integer userId, Integer eventId, Integer comId,
                                    UpdateCommentRequestDto updateCommentRequestDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id = %s not found", userId)));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with comId = %s not found", comId)));
        if (!Objects.equals(comment.getCommentOwner().getId(), userId)) {
            throw new EntityConflictException("No permissions to edit not yours comment");
        }
        updateTextField(comment, updateCommentRequestDto);
        log.info("service. updated comment text, new body = {}", comment);
        return null;
    }

    @Override
    public void deleteComment(Integer userId, Integer eventId, Integer comId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id = %s not found", userId)));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with comId = %s not found", comId)));
        if (!Objects.equals(comment.getCommentOwner().getId(), userId)) {
            throw new EntityConflictException("No permissions to delete not yours comment");
        }
        commentRepository.deleteById(comId);
        log.info("service. successfully deleted comment = {}", comment);
    }

    private void updateTextField(Comment comment, UpdateCommentRequestDto updateCommentRequestDto) {
        if (updateCommentRequestDto.getText() != null && !updateCommentRequestDto.getText().isBlank()) {
            comment.setText(updateCommentRequestDto.getText());
        }
    }
}
