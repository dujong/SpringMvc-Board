package springmvc.board.domain.member.service;

import springmvc.board.domain.member.dto.MemberInfoDto;
import springmvc.board.domain.member.dto.MemberLoginDto;
import springmvc.board.domain.member.dto.MemberSignUpDto;
import springmvc.board.domain.member.dto.MemberUpdateDto;

public interface MemberService {

    /**
     * 회원가입
     * 정보수정
     * 회원탈퇴
     * 정보조회
     */

    String login(MemberLoginDto memberLoginDto) throws Exception;
    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;
    void update(MemberUpdateDto memberUpdateDto) throws Exception;
    void updatePassword(String checkPassword, String toBePassword) throws Exception;
    void withdraw(String checkPassword) throws Exception;
    MemberInfoDto getInfo(Long id) throws Exception;
    MemberInfoDto getMyInfo() throws Exception;

}
