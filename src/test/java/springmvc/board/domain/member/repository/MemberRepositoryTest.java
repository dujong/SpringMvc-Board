package springmvc.board.domain.member.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.member.Role;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @AfterEach
    private void after() {
        em.clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    @Test
    public void 회원저장_성공() throws Exception {
        //given
        Member member = Member.builder().username("username").password("123456").name("Member1").nickName("testA").role(Role.USER).age(26).build();
        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(() -> new RuntimeException("저장된 회원이 없습니다."));

        assertThat(findMember).isSameAs(saveMember);
        assertThat(findMember).isSameAs(member);
    }

    @Test
    public void 오류_회원가입시_아이디가_없음() throws DataIntegrityViolationException {
        //given
        Member member = Member.builder().password("123456").name("Member1").nickName("testA").role(Role.USER).age(26).build();

        //when, then
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_이름이_없음() throws DataIntegrityViolationException {
        Member member = Member.builder().username("username").password("123456").nickName("testA").role(Role.USER).age(26).build();

        //when, then
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_닉네임이_없음() throws Exception {
        //given
        Member member = Member.builder().username("username").password("123456").name("Member1").role(Role.USER).age(26).build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_나이가_없음() throws Exception {
        //given
        Member member = Member.builder().username("username").password("123456").name("Member1").role(Role.USER).nickName("testA").build();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_중복된_아이디가_있음() throws Exception {
        //given
        Member member1 = Member.builder().username("username").password("1234567890").name("Member1").role(Role.USER).nickName("NickName1").age(22).build();
        Member member2 = Member.builder().username("username").password("1111111111").name("Member2").role(Role.USER).nickName("NickName2").age(22).build();

        memberRepository.save(member1);
        clear();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member2));
    }

    @Test
    public void 성공_회원수정() throws Exception {
        //given
        Member member1 = Member.builder().username("username").password("1").name("Member1").role(Role.USER).nickName("NickName1").age(22).build();
        memberRepository.save(member1);
        clear();

        String updatePassword = "updatePassword";
        String updateName = "updateName";
        String updateNickName = "updateNickName";
        int updateAge = 33;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //when
        Member findMember = memberRepository.findById(member1.getId()).orElseThrow(() -> new Exception());
        findMember.updateAge(updateAge);
        findMember.updateName(updateName);
        findMember.updateNickName(updateNickName);
        findMember.updatePassword(passwordEncoder, updatePassword);
        em.flush();

        //then
        Member findUpdateMember = memberRepository.findById(findMember.getId()).orElseThrow(() -> new Exception());

        assertThat(findUpdateMember).isSameAs(findUpdateMember);
        assertThat(passwordEncoder.matches(updatePassword, findMember.getPassword())).isTrue();
        assertThat(findUpdateMember.getName()).isEqualTo(updateName);
        assertThat(findUpdateMember.getName()).isNotEqualTo(member1.getName());
    }

    @Test
    public void 성공_회원삭제() throws Exception {
        //given
        Member member1 = Member.builder().username("username").password("1234567890").name("Member1").role(Role.USER).nickName("NickName1").age(22).build();
        memberRepository.save(member1);
        clear();

        //when
        memberRepository.delete(member1);

        //then
        assertThrows(Exception.class, () -> memberRepository.findById(member1.getId()).orElseThrow(() -> new Exception()));
    }

    @Test
    public void existByUsername_정상작동() throws Exception {
        //given
        String username = "username";
        Member member1 = Member.builder().username(username).password("1234567890").name("Member1").role(Role.USER).nickName("NickName1").age(22).build();
        memberRepository.save(member1);
        clear();

        //when, then
        assertThat(memberRepository.existsByUsername(username)).isTrue();
        assertThat(memberRepository.existsByUsername(username + "123")).isFalse();
    }

    @Test
    public void findByUsername_정상작동() throws Exception {
        //given
        String username = "username";
        Member member1 = Member.builder().username(username).password("123").name("Member1").role(Role.USER).nickName("NickNmae1").age(26).build();
        memberRepository.save(member1);
        clear();

        //when, then
        assertThat(memberRepository.findByUsername("username").get().getUsername()).isEqualTo(member1.getUsername());
        assertThat(memberRepository.findByUsername(username).get().getName()).isEqualTo(member1.getName());
        assertThat(memberRepository.findByUsername(username).get().getId()).isEqualTo(member1.getId());
        assertThrows(Exception.class,
                () -> memberRepository.findByUsername(username + "123")
                        .orElseThrow(() -> new Exception()));

    }

    @Test
    public void 회원가입시_생성시간_등록() throws Exception {
        //given
        Member member1 = Member.builder().username("username").password("1234567890").name("Member1").role(Role.USER).nickName("NickName1").age(22).build();
        memberRepository.save(member1);
        clear();

        //when
        Member findMember = memberRepository.findById(member1.getId()).orElseThrow(() -> new Exception());

        //then
        assertThat(findMember.getCreatedDate()).isNotNull();
        assertThat(findMember.getModifiedDate()).isNotNull();

    }
}