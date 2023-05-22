package springmvc.board.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springmvc.board.domain.User;
import springmvc.board.repository.user.UserRepository;
import springmvc.board.service.user.UserService;

import java.util.NoSuchElementException;

@Slf4j
@RequestMapping("/")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * home
     */
    @GetMapping
    public String home() {
        return "home/index";
    }

    /**
     * Login
     */
    @GetMapping("member/login")
    public String login() {
        return "member/loginForm";
    }

    @PostMapping("member/login")
//    public String login(@ModelAttribute Member member, Model model) {
    public String login(@RequestParam String user_id,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes){
        try {
            User findMember = userRepository.findById(user_id);
            log.info("find member password={} class={}", findMember.getPassword(), findMember.getPassword().getClass());
            log.info("input password={} class={}", password, password.getClass());
            if(findMember.getPassword().equals(password)){
                return "redirect:/board";
            }
            else{
                redirectAttributes.addAttribute("status", true);
                return "redirect:/";
            }
        }
        catch (NoSuchElementException e) {
            log.info("아이디를 찾을 수 없습니다.", e);
            return "redirect:/member/login";
        }

    }

    @GetMapping("member/signup")
    public String signup() {
        return "member/signupForm";
    }
    @GetMapping("member/signupRs")
    public String signupResult() {
        return "member/signupRs";
    }
    @PostMapping("member/signup")
//    public String signup(@ModelAttribute Member member, Model model) {
    public String signup(
            @RequestParam String user_id,
            @RequestParam String password,
            Model model){

        User member = new User(user_id, password);
        userService.join(member);

        model.addAttribute("member", member);

        return "redirect:/member/signupRs";
    }
}
