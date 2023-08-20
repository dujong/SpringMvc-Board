package springmvc.board.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springmvc.board.domain.BaseTimeEntity;
import springmvc.board.domain.comment.Comment;
import springmvc.board.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Table(name = "POST")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(length = 40, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String filePath;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }


    //==게시글을 삭제하면 달려있는 댓글 모두 삭제==//

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void confirmWriter(Member writer) {
        //writer는 변경이 불가능하므로 이정도로 만족
        this.writer = writer;
        writer.addPost(this);
    }

    public void addComment(Comment comment) {
        //comment의 Post 설정은 commnet에서 진행
        commentList.add(comment);
    }

    //== 내용 수정 ==//
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContents(String content) {
        this.content = content;
    }

    public void updateFilePath(String filePath) {
        this.filePath = filePath;
    }

}

