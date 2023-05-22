package springmvc.board.repository.user;

import springmvc.board.domain.User;

import java.util.List;

public interface UserRepository {

    public User save(User member);

    public User findById(String memberId);

    public List<User> findAll();

    public void update(String memberId, String memberPassword);

    public void delete(String memberId);
}
