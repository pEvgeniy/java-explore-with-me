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
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsAdminServiceImpl implements CommentsAdminService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> findAllComments(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        List<Comment> comments = event.getComments();
        log.info("service. found comments for event with id = {}, comments = {}", eventId, comments);
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(Integer comId, Integer eventId, UpdateCommentRequestDto updateCommentRequestDto) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with comId = %s not found", comId)));
        updateTextField(comment, updateCommentRequestDto);
        log.info("service. updated comment text, new body = {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Integer comId, Integer eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with eventId = %s not found", eventId)));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with comId = %s not found", comId)));
        commentRepository.deleteById(comId);
        log.info("service. successfully deleted comment = {}", comment);
    }

    private void updateTextField(Comment comment, UpdateCommentRequestDto updateCommentRequestDto) {
        if (updateCommentRequestDto.getText() != null && !updateCommentRequestDto.getText().isBlank()) {
            comment.setText(updateCommentRequestDto.getText());
        }
    }
}
