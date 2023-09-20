package springmvc.board.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import springmvc.board.domain.member.dto.MemberLoginDto;
import springmvc.board.domain.member.service.MemberService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;

    /**
     * Main 화면
     */
    @GetMapping("/")
    public String main() {
        return "Main";
    }

    /**
     * 로그인
     */
    @GetMapping(value = "/login")
    public String home(MemberLoginDto memberLoginDto, Model model) {
        model.addAttribute("MemberLoginDto", memberLoginDto);
        return "/member/login";
    }
    @PostMapping(value = "/login")
    public String login(@Valid MemberLoginDto memberLoginDto) throws Exception {
        String jwtToken = memberService.login(memberLoginDto);
        if (jwtToken != null) {
            return "로그인 후 이동할 페이지!";
        }
        else { //로그인 실패 시 login으로 재이동
            return "redirect:/login";
        }
    }

}
