package springmvc.board.domain.member;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import springmvc.board.domain.BaseTimeEntity;
import springmvc.board.domain.comment.Comment;
import springmvc.board.domain.post.Post;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Table(name = "MEMBER")
@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;
    private String password;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false, length = 30)
    private String nickName;

    @Column(nullable = false, length = 30)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 1000)
    private String refreshToken;

    //==회원탈퇴 -> 작성한 게시물, 댓글 모두 삭제 ==//
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    //==연관관계 메서드==//
    public void addPost(Post post) {
        //post의 writer 설정은 post에서 함
        postList.add(post);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    // Member 정보 수정
    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }
    public void updateName(String name) {
        this.name = name;
    }

    public void updateAge(int age) {
        this.age = age;
    }
    public void updateNickName(String nickName){
        this.nickName = nickName;
    }

    // JWT
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    // Password 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    //비밀번호 변경, 회원 탈퇴 시, 비밀번호를 확인하며, 이떄 비밀번호의 일치여부를 판단하는 Method
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return passwordEncoder.matches(checkPassword, getPassword());
    }

    //회원가입시, USER의 권한을 부여하는 METHOD
    public void addUserAuthority() {
        this.role = Role.USER;
    }
}
