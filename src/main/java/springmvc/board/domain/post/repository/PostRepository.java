package springmvc.board.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springmvc.board.domain.post.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
