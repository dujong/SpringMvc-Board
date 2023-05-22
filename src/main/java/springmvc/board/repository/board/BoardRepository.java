package springmvc.board.repository.board;
import springmvc.board.domain.Board;

import java.sql.SQLException;
import java.util.List;

public interface BoardRepository {
    public Board save(Board board, String userId) throws SQLException;

    public Board findById(Long board_id) throws SQLException;

    public void update(Board board, String userId) throws SQLException;

    public void delete(Long board_id, String userId) throws SQLException;
}
