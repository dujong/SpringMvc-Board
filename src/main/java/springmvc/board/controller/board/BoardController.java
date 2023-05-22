package springmvc.board.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springmvc.board.domain.Board;
import springmvc.board.repository.board.BoardH2Repository;
import springmvc.board.repository.user.UserRepository;
import springmvc.board.service.board.BoardService;
import springmvc.board.service.user.UserService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardH2Repository boardH2Repository;


    /**
     * board
     */
    @GetMapping
    public String boardHome() {
        return "board/home";
    }

    @GetMapping("/{board_id}")
    public String boardInto(@PathVariable Long board_id, Model model) throws SQLException {
        Board findBoard = boardH2Repository.findById(board_id);
        model.addAttribute("board", findBoard);
        return "board/board_info";

    }

    @GetMapping("/add")
    public String addBoard() {
        return "board/addBoard";
    }

    @PostMapping("/add")
    public String addBoard(@RequestParam String boardName,
                           @RequestParam String boardText,
                           Model model,
                           RedirectAttributes redirectAttributes) throws SQLException {

        Long currentsequence = boardH2Repository.sequence();
        Board board = new Board(currentsequence, boardName, boardText,new Timestamp(System.currentTimeMillis()), 0);
        boardService.write(board, "test");

        log.info("add board_id={}", currentsequence);
        redirectAttributes.addAttribute("board_id", currentsequence);

        return "redirect:/board/{board_id}";

    }


}
