package ru.practicum.controller.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentRequestDto;
import ru.practicum.service.comment.CommentsAdminService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/comments/{comId}/event/{eventId}")
@RequiredArgsConstructor
public class CommentsAdminController {

    private final CommentsAdminService commentsAdminService;

    @GetMapping
    public List<CommentDto> findAllComments(@PathVariable String comId, @PathVariable String eventId) {
        log.info("controller. get. /admin/comments/{}/event/{}. find all comments", comId, eventId);
        return commentsAdminService.findAllComments(comId, eventId);
    }

    @PatchMapping
    public CommentDto updateComment(@PathVariable String comId, @PathVariable String eventId,
                                    @RequestBody @Valid UpdateCommentRequestDto updateCommentRequestDto) {
        log.info("controller. patch. /admin/comments/{}/event/{}. patch comment = {}", comId, eventId, updateCommentRequestDto);
        return commentsAdminService.updateComment(comId, eventId, updateCommentRequestDto);
    }

    @DeleteMapping
    public void deleteComment(@PathVariable String comId, @PathVariable String eventId) {
        log.info("controller. delete. /admin/comments/{}/event/{}. delete comment", comId, eventId);
        commentsAdminService.deleteComment(comId, eventId);
    }
}
