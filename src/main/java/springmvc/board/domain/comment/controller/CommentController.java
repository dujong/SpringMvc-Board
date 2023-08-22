package springmvc.board.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springmvc.board.domain.comment.dto.CommentSaveDto;
import springmvc.board.domain.comment.dto.CommentUpdateDto;
import springmvc.board.domain.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentSave(@PathVariable("postId") Long postId,
                            @ModelAttribute CommentSaveDto commentSaveDto) {
        commentService.save(postId, commentSaveDto);
    }

    @PostMapping("/comment/{postId}/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void reCommentSave(@PathVariable("postId") Long postId,
                              @PathVariable("commentId") Long commentId,
                              @ModelAttribute CommentSaveDto commentSaveDto) {
        commentService.saveReComment(postId, commentId, commentSaveDto);
    }

    @PutMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("commentId") Long commentId,
                       @ModelAttribute CommentUpdateDto commentUpdateDto) {
        commentService.update(commentId, commentUpdateDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long commentId) {
        commentService.remove(commentId);
    }

}
