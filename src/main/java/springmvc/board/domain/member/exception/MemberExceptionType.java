package springmvc.board.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import springmvc.board.global.exception.BaseExceptionType;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements BaseExceptionType {
    //== 회원가입, 로그인 시 ==//
    ALREADY_EXIST_USERNAME(600, HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    WRONG_PASSWORD(601,HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    NOT_FOUND_MEMBER(602, HttpStatus.NOT_FOUND, "회원 정보가 없습니다.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;


}
