//package springmvc.board.controller.board;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import springmvc.board.domain.Board;
//import springmvc.board.repository.board.BoardH2Repository;
//import springmvc.board.repository.board.BoardRepository;
//import springmvc.board.service.board.BoardService;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Slf4j
//@RequestMapping("/board")
////@Controller
//@RequiredArgsConstructor
//public class BoardController {
//
//    private final BoardService boardService;
//    private final BoardRepository boardRepository;
//
//
//    /**
//     * board
//     */
//    @GetMapping
//    public String boardHome(Model model) {
//        try {
//            List<Board> boards = boardRepository.findAll();
//            model.addAttribute("boards", boards);
//        }
//        catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return "board/home";
//    }
//
//    @GetMapping("/{board_id}")
//    public String boardInto(@PathVariable Long board_id, Model model) throws SQLException {
//        boardRepository.hitAdd(board_id);
//        Board findBoard = boardRepository.findById(board_id);
//
//
//        model.addAttribute("board", findBoard);
//        return "board/board_info";
//    }
//
//    @GetMapping("/red/{board_id}")
//    public String boardInfo(@PathVariable Long board_id, Model model) throws SQLException {
//        Board findBoard = boardRepository.findById(board_id);
//        model.addAttribute("board", findBoard);
//        return "board/board_info";
//    }
//
//    @GetMapping("/add")
//    public String addBoard() {
//        return "board/addBoard";
//    }
//
//    @PostMapping("/add")
//    public String addBoard(@RequestParam String board_name,
//                           @RequestParam String board_content,
//                           Model model,
//                           RedirectAttributes redirectAttributes) throws SQLException {
//
//        Long currentsequence = boardRepository.sequence();
//        Board board = new Board(currentsequence, board_name, board_content,new Timestamp(System.currentTimeMillis()), 0);
//        boardService.write(board, "test");
//
//        log.info("add board_id={}", currentsequence);
//        redirectAttributes.addAttribute("board_id", currentsequence);
//
//        return "redirect:/board/red/{board_id}";
//
//    }
//
//    @GetMapping("/{board_id}/edit")
//    public String editBoard(@PathVariable Long board_id, Model model) {
//        try {
//            Board findBoard = boardRepository.findById(board_id);
//            model.addAttribute("board", findBoard);
//        }
//        catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        finally {
//            return "board/editBoard";
//        }
//    }
//
//    @PostMapping("/{board_id}/edit")
//    public String editBoard(@PathVariable Long board_id, @ModelAttribute Board board, RedirectAttributes redirectAttributes) {
//        try {
//            boardRepository.update(board, board_id);
//            redirectAttributes.addAttribute("status", true);
//        }
//        catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        finally {
//            return "redirect:/board/red/{board_id}";
//        }
//    }
//
//    @GetMapping("/{board_id}/delete")
//    public String deleteBoard(@PathVariable Long board_id, Model model) {
//        try {
//            String drop_boardName = boardRepository.findById(board_id).getBoard_name();
//            boardRepository.delete(board_id);
//            model.addAttribute("board_name", drop_boardName);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return "board/deleteBoard";
//    }
//
//}
