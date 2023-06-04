package springmvc.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springmvc.board.domain.member.Member;
import springmvc.board.repository.member.MemberRepository_old;

import java.util.NoSuchElementException;


public class MemberController {
    //private final MemberService memberService;
//    private final MemberRepository_old memberRepository;

    /**
     * home
     */
//    @GetMapping
//    public String home() {
//        return "home/index";
//    }

    /**
     * Login
     */
//    @GetMapping("member/login")
//    public String login() {
//        return "member/loginForm";
//    }
//
//    @PostMapping("member/login")
////    public String login(@ModelAttribute Member member, Model model) {
//    public String login(@RequestParam String member_id,
//                        @RequestParam String password,
//                        RedirectAttributes redirectAttributes){
//        try {
//            Member findMember = memberRepository.findById(member_id);
//            log.info("find member password={} class={}", findMember.getPassword(), findMember.getPassword().getClass());
//            log.info("input password={} class={}", password, password.getClass());
//            if(findMember.getPassword().equals(password)){
//                return "redirect:/board";
//            }
//            else{
//                redirectAttributes.addAttribute("status", true);
//                return "redirect:/";
//            }
//        }
//        catch (NoSuchElementException e) {
//            log.info("아이디를 찾을 수 없습니다.", e);
//            return "redirect:/member/login";
//        }
//
//    }
//
//    @GetMapping("member/signup")
//    public String signup() {
//        return "member/signupForm";
//    }
//    @PostMapping("member/signup")
//    public String signup(
//            @RequestParam String member_id,
//            @RequestParam String password,
//            Model model,
//            RedirectAttributes redirectAttributes){
//
//        Member member = new Member(member_id, password);
//        memberService.join(member);
//
//        model.addAttribute("member", member);
//        redirectAttributes.addAttribute("member_id", member_id);
//
//        return "redirect:/member/signupRs/{member_id}";
//    }

//    @GetMapping("member/signupRs/{member_id}")
//    public String signupResult(@PathVariable String member_id, Model model) {
//        model.addAttribute("member_id", member_id);
//        return "member/signupRs";
//    }

}
