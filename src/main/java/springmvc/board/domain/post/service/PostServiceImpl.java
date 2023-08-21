package springmvc.board.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springmvc.board.domain.member.exception.MemberException;
import springmvc.board.domain.member.exception.MemberExceptionType;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.domain.post.Post;
import springmvc.board.domain.post.cond.PostSearchCondition;
import springmvc.board.domain.post.dto.PostInfoDto;
import springmvc.board.domain.post.dto.PostPagingDto;
import springmvc.board.domain.post.dto.PostSaveDto;
import springmvc.board.domain.post.dto.PostUpdateDto;
import springmvc.board.domain.post.exception.PostException;
import springmvc.board.domain.post.exception.PostExceptionType;
import springmvc.board.domain.post.repository.PostRepository;
import springmvc.board.global.file.service.FileService;
import springmvc.board.global.util.security.SecurityUtil;
import org.springframework.data.domain.Pageable;

import static springmvc.board.domain.post.exception.PostExceptionType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Override
    public void save(PostSaveDto postSaveDto) {
        Post post = postSaveDto.toEntity();
        
        //작성자 저장
        post.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
        
        //작성 시 이미지 있으면 저장
        postSaveDto.uploadFile().ifPresent(
                file -> post.updateFilePath(fileService.save(file))
        );
        //post 저장
        postRepository.save(post);
    }

    @Override
    public void update(Long id, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        checkAuthority(post, NOT_AUTHORITY_UPDATE_POST);

        postUpdateDto.title().ifPresent(post::updateTitle);
        postUpdateDto.content().ifPresent(post::updateContents);

        extractedFile(post);

        postUpdateDto.uploadFile().ifPresentOrElse(
                multipartFile -> post.updateFilePath(fileService.save(multipartFile)),
                () -> post.updateFilePath(null)
        );
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        checkAuthority(post, NOT_AUTHORITY_DELETE_POST);

        extractedFile(post);

        postRepository.delete(post);
    }

    /**
     * Post의 id로 Post 조회
     */
    @Override
    public PostInfoDto getPostInfo(Long id) {
        /**
         * Post + MEMBER 조회 -> 쿼리 1번 발생
         *
         * 댓글&대댓글 리스트 조회 -> 쿼리 1번 발생(POST ID로 찾는 것이므로, IN쿼리가 아닌 일반 where문 발생)
         * (댓글과 대댓글 모두 Comment 클래스이므로, JPA는 구분할 방법이 없어서, 당연히 CommentList에 모두 나오는것이 맞다,
         * 가지고 온 것을 가지고 우리가 구분지어주어야 한다.)
         *
         * 댓글 작성자 정보 조회 -> 배치사이즈를 이용했기때문에 쿼리 1번 혹은 N/배치사이즈 만큼 발생
         *
         *
         */
        return new PostInfoDto(postRepository.findWithWriterById(id).orElseThrow(() -> new PostException(POST_NOT_FOUND)));
    }

    @Override
    public PostPagingDto getPostList(Pageable pageable, PostSearchCondition postSearchCondition) {
        return new PostPagingDto(postRepository.search(postSearchCondition, pageable));
    }

    private void checkAuthority(Post post, PostExceptionType postExceptionType) {
        if (!post.getWriter().getUsername().equals(SecurityUtil.getLoginUsername())) {
            throw new PostException(postExceptionType);
        }
    }

    private void extractedFile(Post post) {
        if (post.getFilePath() != null) {
            fileService.delete(post.getFilePath()); //기존에 올린 파일 삭제
        }
    }
}
