package springmvc.board.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springmvc.board.domain.Board;
import springmvc.board.repository.board.BoardRepository;

import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board write(Board board, String user_id) throws SQLException {
        Board newBoard = boardRepository.save(board, user_id);
        return newBoard;
    }
}
