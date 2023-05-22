package springmvc.board.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class Board {
    private Long board_id;
    private String board_name;
    private String board_content;

    private Timestamp time;
    private Integer hit;

    public Board() {
    }

    public Board(Long board_id, String board_name, String board_content, Timestamp time, Integer hit) {
        this.board_id = board_id;
        this.board_name = board_name;
        this.board_content = board_content;
        this.time = time;
        this.hit = hit;
    }
}
