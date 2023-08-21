package springmvc.board.domain.comment.exception;

import org.springframework.http.HttpStatus;
import springmvc.board.global.exception.BaseExceptionType;

public enum CommentExceptionType implements BaseExceptionType {

    NOT_POUND_COMMENT(800, HttpStatus.NOT_FOUND, "찾으시는 댓글이 없습니다"),
    NOT_AUTHORITY_UPDATE_COMMENT(801, HttpStatus.FORBIDDEN, "댓글을 업데이트할 권한이 없습니다."),
    NOT_AUTHORITY_DELETE_COMMENT(802, HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    CommentExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
