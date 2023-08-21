package springmvc.board.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import springmvc.board.domain.post.Post;

@Data
@NoArgsConstructor
public class BriefPostInfo {
    private Long postId;


    private String title;//제목
    private String content;//내용
    private String writerName;//작성자
    private String createdDate;//작성일
    public BriefPostInfo(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerName = post.getWriter().getName();
        this.createdDate = post.getCreatedDate().toString();
    }
}
