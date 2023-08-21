package springmvc.board.domain.post.cond;

import lombok.Data;

@Data
public class PostSearchCondition {
    private String title;
    private String content;
}
