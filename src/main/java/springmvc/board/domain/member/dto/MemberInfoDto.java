package springmvc.board.domain.member.dto;

import lombok.Builder;
import lombok.Data;
import springmvc.board.domain.member.Member;

@Data
public class MemberInfoDto {
    private final String name;
    private final String nickName;
    private final String username;
    private final Integer age;

    @Builder
    public MemberInfoDto(Member member) {
        this.name = member.getName();
        this.nickName = member.getNickname();
        this.username = member.getUsername();
        this.age = member.getAge();
    }
}
