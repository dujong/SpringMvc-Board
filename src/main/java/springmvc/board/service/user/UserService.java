package springmvc.board.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springmvc.board.domain.User;
import springmvc.board.repository.user.UserH2Repository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserH2Repository userRepository;

    //회원가입
    public User join(User user) {
        User newUser = userRepository.save(user);
        return newUser;
    }

}
