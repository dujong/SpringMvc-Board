package springmvc.board.global.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThreadLocalLogTrace implements LogTrace{
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        long startTimeMs = System.currentTimeMillis();

        log.info("[{}] {}{}",
                traceId.getId(),
                addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                        message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(
                    (i == level - 1)
                            ? "|" + prefix
                            : "| "
            );
        }
        return sb.toString();
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }
    @Override
    public void exception(TraceStatus status, Throwable e) {
        complete(status, e);
    }

    private void complete(TraceStatus traceStatus, Throwable e) {
        long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - traceStatus.getStartTimeMs();
        TraceId traceId = traceStatus.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    traceStatus.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    traceStatus.getMessage(),
                    resultTimeMs,
                    e.toString());
        }
        releaseTraceId();
    }
    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if(traceId.isFirstLevel()){
            traceIdHolder.remove();
        }else{
            traceIdHolder.set(traceId.createPreviousId());
        }

    }
}
