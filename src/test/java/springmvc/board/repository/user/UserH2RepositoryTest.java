package springmvc.board.repository.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springmvc.board.domain.User;

import javax.sql.DataSource;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserH2RepositoryTest {
    @Autowired
    private UserH2Repository userRepository;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        UserH2Repository userRepository() {
            return new UserH2Repository(dataSource);
        }
    }

    @AfterEach
    void after() {
        userRepository.clear();
    }

    @Test
    @DisplayName("회원가입")
    void create() {
        //given
        User userA = new User("userA", "123");
        User userB = new User("userB", "sifal");

        //when
        userRepository.save(userA);
        userRepository.save(userB);

        //then
        assertThat(userRepository.findAll().size()).isEqualTo(2);
        assertThat(userRepository.findAll()).contains(userA, userB);
    }

    @Test
    @DisplayName("회원 조회")
    void findById() {
        //given
        User userA = new User("userA", "123");
        User userB = new User("userB", "sifal");
        userRepository.save(userA);
        userRepository.save(userB);

        //when
        User findMember = userRepository.findById(userA.getUser_id());

        //then
        assertThat(findMember).isEqualTo(userA);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void update() {
        //given
        User userA = new User("userA", "123");
        User userB = new User("userB", "sifal");
        userRepository.save(userA);
        userRepository.save(userB);

        //when
        userRepository.update(userA.getUser_id(), "102030");

        //then
        User findMember = userRepository.findById(userA.getUser_id());
        assertThat(findMember.getPassword()).isEqualTo("102030");
    }

    @Test
    @DisplayName("회원 삭제")
    void delete() {
        //given
        User userA = new User("userA", "123");
        User userB = new User("userB", "sifal");
        userRepository.save(userA);
        userRepository.save(userB);

        //when
        userRepository.delete(userA.getUser_id());

        //then
        assertThatThrownBy(() -> userRepository.findById(userA.getUser_id()))
                .isInstanceOf(NoSuchElementException.class);
    }
}