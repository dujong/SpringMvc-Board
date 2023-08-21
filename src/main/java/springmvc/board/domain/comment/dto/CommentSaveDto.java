package springmvc.board.domain.comment.dto;

import springmvc.board.domain.comment.Comment;

public record CommentSaveDto(String content) {
    public Comment toEntity() {
        return Comment.builder().content(content).build();
    }
}
