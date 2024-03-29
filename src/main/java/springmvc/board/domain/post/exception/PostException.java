package springmvc.board.domain.post.exception;

import springmvc.board.global.exception.BaseException;
import springmvc.board.global.exception.BaseExceptionType;

public class PostException extends BaseException {
    private BaseExceptionType exceptionType;

    public PostException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
