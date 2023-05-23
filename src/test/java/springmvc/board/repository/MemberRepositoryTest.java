package springmvc.board.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import springmvc.board.domain.Member;
import springmvc.board.repository.member.MemberH2Repository;

import javax.sql.DataSource;

class MemberRepositoryTest {
    private DataSource dataSource;
    private MemberH2Repository userRepository;

    @BeforeEach
    void before() {
        dataSource = new DriverManagerDataSource("jdbc:h2:tcp://localhost/~/test", "sa", "");
        userRepository = new MemberH2Repository(dataSource);
    }

    @Test
    @DisplayName("회원가입")
    void save() {
        Member memberA = new Member("userA", "123");
        Member memberB = new Member("userB", "sifal");

        userRepository.save(memberA);
        userRepository.save(memberB);


    }

}