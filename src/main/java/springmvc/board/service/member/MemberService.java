//package springmvc.board.service.member;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import springmvc.board.domain.member.Member;
//import springmvc.board.repository.member.MemberH2Repository;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class MemberService {
//    private final MemberH2Repository userRepository;
//
//    //회원가입
//    public Member join(Member member) {
//        Member newUser = userRepository.save(member);
//        return newUser;
//    }
//
//}
