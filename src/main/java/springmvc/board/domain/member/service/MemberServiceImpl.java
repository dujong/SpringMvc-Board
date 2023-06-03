package springmvc.board.domain.member.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.jaxb.internal.MappingBinder;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.member.dto.MemberInfoDto;
import springmvc.board.domain.member.dto.MemberSignUpDto;
import springmvc.board.domain.member.dto.MemberUpdateDto;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.global.util.security.SecurityUtil;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = Member.builder()
                .username(memberSignUpDto.getUsername())
                .nickname(memberSignUpDto.getNickName())
                .password(memberSignUpDto.getPassword())
                .name(memberSignUpDto.getName())
                .age(memberSignUpDto.getAge()).build();

        member.addUserAuthority();
        member.encodePassword(passwordEncoder);

        if(memberRepository.findByUsername(memberSignUpDto.getUsername()).isPresent()){
            throw new DuplicateRequestException("이미 존재하는 아이디입니다.");
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto) throws Exception {
        Member member = findMember();
        if (memberUpdateDto.getAge() != -1) {
            member.updateAge(memberUpdateDto.getAge());
        }
        if (memberUpdateDto.getName() != null) {
            member.updateName(memberUpdateDto.getName());
        }
        if (memberUpdateDto.getNickName() != null) {
            member.updateNickName(memberUpdateDto.getNickName());
        }
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
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new Exception("회원이 없습니다"));
        return new MemberInfoDto(findMember);
    }
    @Override
    public MemberInfoDto getMyInfo() throws Exception {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new Exception("회원이 없습니다"));
        return new MemberInfoDto(findMember);
    }

    private Member findMember() throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));
        return member;
    }
    private void checkPassword(String checkPassword, Member member) throws Exception {
        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
    }

}
