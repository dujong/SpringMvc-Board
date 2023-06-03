package springmvc.board.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springmvc.board.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
