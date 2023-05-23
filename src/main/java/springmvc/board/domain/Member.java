package springmvc.board.domain;

import lombok.Data;

@Data
public class Member {
//    private Long member_Idx;
    private String member_id;
    private String password;
//    private String email;
//    private String name;

//    private String phone;
//    private String sex;
//    private MemberStatus status;

    public Member() {
    }

    public Member(String member_id, String password) {
        this.member_id = member_id;
        this.password = password;
    }
}
