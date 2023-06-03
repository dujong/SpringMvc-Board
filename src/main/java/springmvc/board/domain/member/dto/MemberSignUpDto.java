package springmvc.board.domain.member.dto;

import lombok.Builder;
import lombok.Data;
import springmvc.board.domain.member.Member;

@Data
public class MemberSignUpDto {
    private final String name;
    private final String nickName;
    private final String username;
    private final String password;
    private final Integer age;

    public MemberSignUpDto(String name, String nickName, String username, String password, Integer age) {
        this.name = name;
        this.nickName = nickName;
        this.username = username;
        this.password = password;
        this.age = age;
    }

    @Builder
    public MemberSignUpDto(Member member) {
        this.name = member.getName();
        this.nickName = member.getNickname();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.age = member.getAge();
    }
}
