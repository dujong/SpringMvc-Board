package springmvc.board.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springmvc.board.domain.post.Post;
import springmvc.board.domain.post.cond.PostSearchCondition;

public interface CustomPostRepository {
    Page<Post> search(PostSearchCondition postSearchCondition, Pageable pageable);
}
