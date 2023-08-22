package springmvc.board.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import springmvc.board.global.log.LogTrace;
import springmvc.board.global.log.TraceStatus;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAop {
    private final LogTrace logTrace;

    @Pointcut("execution(* springmvc.board.domain..*Service*.*(..))")
    public void allService() {
    }

    ;

    @Pointcut("execution(* springmvc.board.domain..*Repository*.*(..))")
    public void allRepository() {
    }

    ;

    @Pointcut("execution(* springmvc.board.domain..*Controller*.*(..))")
    public void allController() {
    }

    ;

    @Around("allService() || allController() || allRepository()")
    public Object logTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            status = logTrace.begin(joinPoint.getSignature().toShortString());
            Object result = joinPoint.proceed();
            logTrace.end(status);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            logTrace.exception(status, e);
            throw e;
        }

    }
}
