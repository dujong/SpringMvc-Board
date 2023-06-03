package springmvc.board.domain.member;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import springmvc.board.domain.BaseTimeEntity;

import javax.persistence.*;

@Table(name = "MEMBER")
@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;
    private String password;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 30)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 1000)
    private String refreshToken;

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
    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    // Password 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    // JWT
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken() {
        this.refreshToken = null;
    }
}
