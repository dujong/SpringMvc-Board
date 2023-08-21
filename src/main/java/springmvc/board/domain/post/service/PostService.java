package springmvc.board.domain.post.service;

import springmvc.board.domain.post.cond.PostSearchCondition;
import springmvc.board.domain.post.dto.PostInfoDto;
import springmvc.board.domain.post.dto.PostPagingDto;
import springmvc.board.domain.post.dto.PostSaveDto;
import springmvc.board.domain.post.dto.PostUpdateDto;
import springmvc.board.domain.post.exception.PostException;
import org.springframework.data.domain.Pageable;

public interface PostService {
    /**
     * 게시글 등록
     */
    void save(PostSaveDto postSaveDto) throws PostException;

    /**
     * 게시글 수정
     */
    void update(Long id, PostUpdateDto postUpdateDto);

    /**
     * 게시글 삭제
     */
    void delete(Long id);

    /**
     * 게시글 조회
     */
    PostInfoDto getPostInfo(Long id);

    PostPagingDto getPostList(Pageable pageable, PostSearchCondition postSearchCondition);

}
