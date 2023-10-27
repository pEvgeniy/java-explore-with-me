package ru.practicum.controller.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;
import ru.practicum.service.comment.CommentsPrivateService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentsPrivateController {

    private final CommentsPrivateService commentsPrivateService;

    @PostMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                                    @RequestBody @Valid CommentDto commentDto) {
        log.info("controller. post. /users/{}/comments/event/{}. create comment = {}", userId, eventId, commentDto);
        return commentsPrivateService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{comId}/event/{eventId}")
    public CommentDto updateComment(@PathVariable Integer userId, @PathVariable Integer comId, @PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateCommentRequestDto updateCommentRequestDto) {
        log.info("controller. patch. /users/{}/comments/{}/event/{}. update comment = {}", userId, comId, eventId, updateCommentRequestDto);
        return commentsPrivateService.updateComment(userId, eventId, comId, updateCommentRequestDto);
    }

    @DeleteMapping("/{comId}/event/{eventId}")
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer comId, @PathVariable Integer eventId) {
        log.info("controller. delete. /users/{}/comments/{}/event/{}. delete comment", userId, comId, eventId);
        commentsPrivateService.deleteComment(userId, eventId, comId);
    }
}
