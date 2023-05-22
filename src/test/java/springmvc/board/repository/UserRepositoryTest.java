package springmvc.board.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import springmvc.board.domain.User;
import springmvc.board.repository.user.UserH2Repository;

import javax.sql.DataSource;

class UserRepositoryTest {
    private DataSource dataSource;
    private UserH2Repository userRepository;

    @BeforeEach
    void before() {
        dataSource = new DriverManagerDataSource("jdbc:h2:tcp://localhost/~/test", "sa", "");
        userRepository = new UserH2Repository(dataSource);
    }

    @Test
    @DisplayName("회원가입")
    void save() {
        User userA = new User("userA", "123");
        User userB = new User("userB", "sifal");

        userRepository.save(userA);
        userRepository.save(userB);


    }

}