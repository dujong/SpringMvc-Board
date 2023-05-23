package springmvc.board.repository.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springmvc.board.domain.Member;

import javax.sql.DataSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberH2RepositoryTest {
    @Autowired
    private MemberH2Repository memberH2Repository;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberH2Repository memberH2Repository() {
            return new MemberH2Repository(dataSource);
        }
    }

    @AfterEach
    void after() {
        memberH2Repository.clear();
    }

    @Test
    @DisplayName("회원가입")
    void create() {
        //given
        Member memberA = new Member("userA", "123");
        Member memberB = new Member("userB", "sifal");

        //when
        memberH2Repository.save(memberA);
        memberH2Repository.save(memberA);

        //then
        assertThat(memberH2Repository.findAll().size()).isEqualTo(2);
        assertThat(memberH2Repository.findAll()).contains(memberA, memberB);
    }

    @Test
    @DisplayName("회원 조회")
    void findById() {
        //given
        Member memberA = new Member("userA", "123");
        Member memberB = new Member("userB", "sifal");
        memberH2Repository.save(memberA);
        memberH2Repository.save(memberB);

        //when
        Member findMember = memberH2Repository.findById(memberA.getMember_id());

        //then
        assertThat(findMember).isEqualTo(memberA);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void update() {
        //given
        Member memberA = new Member("userA", "123");
        Member memberB = new Member("userB", "sifal");
        memberH2Repository.save(memberA);
        memberH2Repository.save(memberB);

        //when
        memberH2Repository.update(memberA.getMember_id(), "102030");

        //then
        Member findMember = memberH2Repository.findById(memberA.getMember_id());
        assertThat(findMember.getPassword()).isEqualTo("102030");
    }

    @Test
    @DisplayName("회원 삭제")
    void delete() {
        //given
        Member memberA = new Member("userA", "123");
        Member memberB = new Member("userB", "sifal");
        memberH2Repository.save(memberA);
        memberH2Repository.save(memberB);

        //when
        memberH2Repository.delete(memberA.getMember_id());

        //then
        assertThatThrownBy(() -> memberH2Repository.findById(memberA.getMember_id()))
                .isInstanceOf(NoSuchElementException.class);
    }
}