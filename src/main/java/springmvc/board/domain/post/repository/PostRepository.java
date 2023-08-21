package springmvc.board.domain.post.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springmvc.board.domain.post.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>,CustomPostRepository{
    @EntityGraph(attributePaths = {"writer"})
    Optional<Post> findWithWriterById(Long id);

}
