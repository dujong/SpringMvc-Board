package springmvc.board.domain.comment.dto;

import java.util.Optional;

public record CommentUpdateDto(Optional<String> content) {

}
