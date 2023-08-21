package springmvc.board.domain.comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springmvc.board.domain.BaseTimeEntity;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.post.Post;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.FetchType.*;

@Table(name = "COMMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean isRemoved = false;

    //== 부모 댓글을 삭제해도 자식 댓글은 남아있음 ==//
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    //== 연관관계 편의 메서드 ==//
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addComment(this);
    }

    public void confirmPost(Post post) {
        this.post = post;
        post.addComment(this);
    }
    public void confirmParent(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child) {
        childList.add(child);
    }

    //== 수정 ==//
    public void updateContent(String content) {
        this.content = content;
    }
    public void remove() {
        this.isRemoved = true;
    }

    @Builder
    public Comment(Member writer, Post post, Comment parent, String content, boolean isRemoved) {
        this.writer = writer;
        this.post = post;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }

    //== 비즈니스 로직 ==//
    public List<Comment> findRemovableList() {
        List<Comment> result = new ArrayList<>();
        Optional.ofNullable(this.parent).ifPresentOrElse(
            parentComment -> {
                //대댓글인 경우 (부모가 존재하는 경우)
                if(parentComment.isRemoved() && parentComment.isAllChildRemoved()){
                    result.addAll(parentComment.getChildList());
                    result.add(parentComment);
                }
            },

            () -> {//댓글인 경우
                if (isAllChildRemoved()) {
                    result.add(this);
                    result.addAll(this.getChildList());

                }
            }
        );
        return result;
    }

    //모든 자식 댓글이 삭제되었는지 Check
    private boolean isAllChildRemoved() {
        return getChildList().stream()
                .map(Comment::isRemoved)
                .filter(isRemoved -> !isRemoved)
                .findAny()
                .orElse(true);
    }
}
