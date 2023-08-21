package springmvc.board.domain.comment.dto;

import lombok.Data;
import springmvc.board.domain.comment.Comment;
import springmvc.board.domain.member.dto.MemberInfoDto;

import java.util.List;

@Data
public class CommentInfoDto {
    private final static String DEFAULT_DELETE_MESSAGE = "삭제된 댓글입니다";

    private Long postId;//댓글이 달린 POST의 ID
    private Long commentId;//해당 댓글의 ID
    private String content;//내용 (삭제되었다면 "삭제된 댓글입니다" 출력)
    private boolean isRemoved;//삭제되었는지?

    private MemberInfoDto writeDto;//댓글 작성자에 대한 정보

    private List<ReCommentInfoDto> reCommentInfoDtoList;//대댓글에 대한 정보들

    /**
     * 삭제되었을 경우 출력글
     */
    public CommentInfoDto(Comment comment, List<Comment> reCommentList) {
        this.postId = comment.getPost().getId();
        this.commentId = comment.getId();
        this.content = comment.getContent();

        if (comment.isRemoved()) {
            this.content = DEFAULT_DELETE_MESSAGE;
        }

        this.isRemoved = comment.isRemoved();

        this.writeDto = new MemberInfoDto(comment.getWriter());
        this.reCommentInfoDtoList = reCommentList.stream().map(ReCommentInfoDto::new).toList();
    }
}
