package springmvc.board.repository.board;
import springmvc.board.domain.Board;

import java.sql.SQLException;
import java.util.List;

public interface BoardRepository {
    public Board save(Board board, String userId) throws SQLException;

    public Board findById(Long board_id) throws SQLException;

    public List<Board> findAll() throws SQLException;

    public void update(Board board, Long board_id) throws SQLException;

    public void delete(Long board_id) throws SQLException;

    public Long sequence() throws SQLException;

    public void hitAdd(Long board_id) throws SQLException;
}
