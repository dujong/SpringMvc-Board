package springmvc.board.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.member.dto.MemberInfoDto;
import springmvc.board.domain.member.dto.MemberSignUpDto;
import springmvc.board.domain.member.dto.MemberUpdateDto;
import springmvc.board.domain.member.exception.MemberException;
import springmvc.board.domain.member.exception.MemberExceptionType;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.global.util.security.SecurityUtil;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
//        Member member = Member.builder()
//                .username(memberSignUpDto.username())
//                .nickName(memberSignUpDto.nickName())
//                .password(memberSignUpDto.password())
//                .name(memberSignUpDto.name())
//                .age(memberSignUpDto.age()).build();

        Member member = memberSignUpDto.toEntity();

        member.addUserAuthority();
        member.encodePassword(passwordEncoder);

        if(memberRepository.findByUsername(memberSignUpDto.username()).isPresent()){
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto) throws Exception {
        Member member = findMember();

        memberUpdateDto.age().ifPresent(member::updateAge);
        memberUpdateDto.name().ifPresent(member::updateName);
        memberUpdateDto.nickName().ifPresent(member::updateNickName);
    }

    @Override
    public void updatePassword(String checkPassword, String toBePassword) throws Exception {
        Member member = findMember();
        checkPassword(checkPassword, member);
        member.updatePassword(passwordEncoder, toBePassword);

    }

    @Override
    public void withdraw(String checkPassword) throws Exception {
        Member member = findMember();
        checkPassword(checkPassword, member);
        memberRepository.delete(member);
    }

    @Override
    public MemberInfoDto getInfo(Long id) throws Exception {
//        Member findMember = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
//        return new MemberInfoDto(findMember);
        return new MemberInfoDto(memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
    }

    @Override
    public MemberInfoDto getMyInfo() throws Exception {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberInfoDto(findMember);
    }

    private Member findMember() throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return member;
    }
    private void checkPassword(String checkPassword, Member member) throws Exception {
        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            log.info("checkPassword Error 발생");
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }
    }

}
