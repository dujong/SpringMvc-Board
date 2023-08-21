package springmvc.board.domain.comment.exception;

import springmvc.board.global.exception.BaseException;
import springmvc.board.global.exception.BaseExceptionType;

public class CommentException extends BaseException {
    private BaseExceptionType exceptionType;

    public CommentException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }


    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
