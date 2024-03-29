package springmvc.board.domain.member.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springmvc.board.domain.member.dto.*;
import springmvc.board.domain.member.service.MemberService;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//@RestController
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원가입
     */
    @GetMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public String signUp(MemberSignUpDto memberSignUpDto, Model model) throws Exception {
        model.addAttribute("MemberSignUpDto", memberSignUpDto);
        return "/login/signupForm";
    }
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public String signUp(@RequestPart(value = "key") @Valid MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return "/member/login";
    }

    /**
     * 회원정보수정
     */
    @PutMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void updateBasicInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
        memberService.update(memberUpdateDto);
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/member/password")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.checkPassword(),updatePasswordDto.toBePassword());
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody MemberWithDrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.checkPassword());
    }

    /**
     * 회원정보조회
     */
    @GetMapping("/member/{id}")
    public ResponseEntity getInfo(@Valid @PathVariable("id") Long id) throws Exception {
        MemberInfoDto info = memberService.getInfo(id);
        return new ResponseEntity(info, HttpStatus.OK);
    }

    /**
     * 내 정보조회
     */
    @GetMapping("/member")
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {
        MemberInfoDto myInfo = memberService.getMyInfo();
        return new ResponseEntity(myInfo, HttpStatus.OK);
    }
}
