package springmvc.board.domain.member.dto;

import lombok.Builder;
import lombok.Data;
import springmvc.board.domain.member.Member;

@Data
public class MemberUpdateDto {
    private final String name;
    private final String nickName;
    private final Integer age;

    public MemberUpdateDto(String name, String nickName, Integer age) {
        this.name = name;
        this.nickName = nickName;
        this.age = age;
    }

    @Builder
    public MemberUpdateDto(Member member) {
        this.name = member.getName();
        this.nickName = member.getNickname();
        this.age = member.getAge();
    }
}
