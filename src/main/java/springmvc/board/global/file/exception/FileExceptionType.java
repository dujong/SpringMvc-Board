package springmvc.board.global.file.exception;

import org.springframework.http.HttpStatus;
import springmvc.board.global.exception.BaseExceptionType;

public enum FileExceptionType implements BaseExceptionType {
    FILE_CAN_NOT_SAVE(10000, HttpStatus.BAD_REQUEST, "파일 저장에 실패했습니다."),
    FILE_CAN_NOT_DELETE(10001, HttpStatus.BAD_REQUEST, "파일 삭제에 실패했습니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    FileExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
