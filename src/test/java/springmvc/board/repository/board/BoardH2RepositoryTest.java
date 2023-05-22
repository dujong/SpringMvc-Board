package springmvc.board.repository.board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springmvc.board.domain.Board;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardH2RepositoryTest {
    @Autowired
    private BoardH2Repository boardRepository;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        BoardH2Repository boardRepository() {
            return new BoardH2Repository(dataSource);
        }
    }

//    @AfterEach
//    void after() {
//        boardRepository.clear();
//    }

    @Test
    @DisplayName("게시글 등록")
    void create() throws SQLException {
        boardRepository.clear();

        //given
        Board board1 = new Board(1L, "누가좀 살려줘..", "아니 이건 아니자나 진짜로??", new Timestamp(System.currentTimeMillis()), 10);
        Board board2 = new Board(2L, "난 살았따.. 흐흐", "억까 금지요~~", new Timestamp(System.currentTimeMillis()), 20);
        Board board2_1 = new Board(3L, "후기 남겨요~", "버그썻음...", new Timestamp(System.currentTimeMillis()), 1200);

        //when
        boardRepository.save(board1, "JD1");
        boardRepository.save(board2, "JD2");
        boardRepository.save(board2_1, "JD2");


        //then
        assertThat(boardRepository.findById(1L)).isEqualTo(board1);
        assertThat(boardRepository.findById(2L)).isEqualTo(board2);
    }

    @Test
    @DisplayName("게시글 조회")
    void findById() throws SQLException {
        //given
        Board board1 = new Board(1L, "누가좀 살려줘..", "아니 이건 아니자나 진짜로??", new Timestamp(System.currentTimeMillis()), 10);
        Board board2 = new Board(2L, "난 살았따.. 흐흐", "억까 금지요~~", new Timestamp(System.currentTimeMillis()), 20);
        Board board2_1 = new Board(3L, "후기 남겨요~", "버그썻음...", new Timestamp(System.currentTimeMillis()), 1200);
        boardRepository.save(board1, "JD1");
        boardRepository.save(board2, "JD2");
        boardRepository.save(board2_1, "JD2");

        //when
        List<Board> boards = new ArrayList<>();
        boards.add(boardRepository.findById(2L));
        boards.add(boardRepository.findById(3L));

        //then
        assertThat(boards).contains(board2, board2_1);
    }

    @Test
    @DisplayName("게시글 변경")
    void update() throws SQLException {
        //given
        Board board1 = new Board(1L, "누가좀 살려줘..", "아니 이건 아니자나 진짜로??", new Timestamp(System.currentTimeMillis()), 10);
        Board board1_1 = new Board(100L, "나도 살았따~~!!", "아니 이건 아니자나 진짜로??", new Timestamp(System.currentTimeMillis()), 102);
        Board board2 = new Board(2L, "난 살았따.. 흐흐", "억까 금지요~~", new Timestamp(System.currentTimeMillis()), 20);
        Board board2_1 = new Board(3L, "후기 남겨요~", "버그썻음...", new Timestamp(System.currentTimeMillis()), 1200);
        boardRepository.save(board1, "JD1");
        boardRepository.save(board2, "JD2");
        boardRepository.save(board2_1, "JD2");

        //when
        boardRepository.update(board1_1, "JD1");
        Board jd1Boards = boardRepository.findById(1L);

        //then
        assertThat(jd1Boards).isEqualTo(board1);
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() throws SQLException {
        //given
        Board board1 = new Board(1L, "누가좀 살려줘..", "아니 이건 아니자나 진짜로??", new Timestamp(System.currentTimeMillis()), 10);
        Board board1_1 = new Board(1L, "나도 살았따~~!!", "아니 이건 아니자나 진짜로??", new Timestamp(System.currentTimeMillis()), 102);
        Board board2 = new Board(2L, "난 살았따.. 흐흐", "억까 금지요~~", new Timestamp(System.currentTimeMillis()), 20);
        Board board2_1 = new Board(3L, "후기 남겨요~", "버그썻음...", new Timestamp(System.currentTimeMillis()), 1200);
        boardRepository.save(board1, "JD1");
        boardRepository.save(board2, "JD2");
        boardRepository.save(board2_1, "JD2");

        //when
        boardRepository.delete(1L, "JD1");

        //then
        assertThatThrownBy(() -> boardRepository.findById(1L))
                .isInstanceOf(NoSuchElementException.class);
    }


}