package springmvc.board.domain;

import lombok.Data;

@Data
public class User {
//    private Long member_Idx;
    private String user_id;
    private String password;
//    private String email;
//    private String name;

//    private String phone;
//    private String sex;
//    private MemberStatus status;

    public User() {
    }

    public User(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }
}
