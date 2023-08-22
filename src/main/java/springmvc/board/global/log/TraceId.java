package springmvc.board.global.log;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import springmvc.board.global.util.security.SecurityUtil;

import java.util.UUID;

@Getter
public class TraceId {


    private String id;
    private int level;//깊이

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }
    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        try {
            SecurityUtil.getLoginUsername();
        } catch (NullPointerException | ClassCastException e) {//로그인 안하고 접근 & SingUp 동일
            return String.format("[Anonymous: %s]", UUID.randomUUID().toString().substring(0, 8));
        }
        return SecurityUtil.getLoginUsername();
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }


}
