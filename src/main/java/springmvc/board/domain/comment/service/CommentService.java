package springmvc.board.domain.comment.service;

import lombok.extern.java.Log;
import springmvc.board.domain.comment.Comment;
import springmvc.board.domain.comment.dto.CommentSaveDto;
import springmvc.board.domain.comment.dto.CommentUpdateDto;
import springmvc.board.domain.comment.exception.CommentException;

import java.util.List;

public interface CommentService {

    void save(Long postId, CommentSaveDto commentSaveDto);

    void saveReComment(Long postId, Long parentId, CommentSaveDto commentSaveDto);

    void update(Long id, CommentUpdateDto commentUpdateDto);

    void remove(Long id) throws CommentException;
}
