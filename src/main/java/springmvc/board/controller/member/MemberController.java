//package springmvc.board.controller.member;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import springmvc.board.domain.member.dto.*;
//import springmvc.board.domain.member.repository.MemberRepository;
//import springmvc.board.domain.member.service.MemberService;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class MemberController {
//    private final MemberService memberService;
//    private final MemberRepository memberRepository;
//
//    /**
//     * 회원가입
//     */
//    @PostMapping("/signUp")
//    @ResponseStatus(HttpStatus.OK)
//    public void signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
//        memberService.signUp(memberSignUpDto);
//    }
//
//    /**
//     * 회원정보 수정
//     */
//    @PutMapping("/member")
//    @ResponseStatus(HttpStatus.OK)
//    public void updateBasicInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
//        memberService.update(memberUpdateDto);
//    }
//
//    /**
//     * 비밀번호 수정
//     */
//    @PutMapping("/member/password")
//    @ResponseStatus(HttpStatus.OK)
//    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
//        memberService.updatePassword(updatePasswordDto.checkPassword(), updatePasswordDto.toBePassword());
//    }
//
//    /**
//     * 회원탈퇴
//     */
//    @DeleteMapping("/member")
//    @ResponseStatus(HttpStatus.OK)
//    public void withdraw(@Valid @RequestBody MemberWithDrawDto memberWithDrawDto) throws Exception {
//        memberService.withdraw(memberWithDrawDto.checkPassword());
//    }
//
//    /**
//     * 회원정보 조회
//     */
//    @GetMapping("/member/{id}")
//    public ResponseEntity getInfo(@Valid @PathVariable("id") Long id) throws Exception {
//        MemberInfoDto info = memberService.getInfo(id);
//        return new ResponseEntity(info, HttpStatus.OK);
//    }
//
//    /**
//     * 내 정보조회
//     */
//    @GetMapping("/member")
//    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {
//        MemberInfoDto myInfo = memberService.getMyInfo();
//        return new ResponseEntity(myInfo, HttpStatus.OK);
//    }
//
//}
