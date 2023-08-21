package springmvc.board.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springmvc.board.domain.comment.Comment;
import springmvc.board.domain.comment.dto.CommentSaveDto;
import springmvc.board.domain.comment.dto.CommentUpdateDto;
import springmvc.board.domain.comment.exception.CommentException;
import springmvc.board.domain.comment.exception.CommentExceptionType;
import springmvc.board.domain.comment.repository.CommentRepository;
import springmvc.board.domain.member.exception.MemberException;
import springmvc.board.domain.member.exception.MemberExceptionType;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.domain.post.exception.PostException;
import springmvc.board.domain.post.exception.PostExceptionType;
import springmvc.board.domain.post.repository.PostRepository;
import springmvc.board.global.util.security.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void save(Long postId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmPost(postRepository.findById(postId).orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND)));

        commentRepository.save(comment);
    }

    @Override
    public void saveReComment(Long postId, Long parentId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
        comment.confirmPost(postRepository.findById(postId).orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND)));
        comment.confirmParent(commentRepository.findById(parentId).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT)));

        commentRepository.save(comment);
    }

    @Override
    public void update(Long id, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT));

        if (!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUsername())) {
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_UPDATE_COMMENT);
        }

        commentUpdateDto.content().ifPresent(comment::updateContent);
    }


    @Override
    public void remove(Long id) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT));

        if (!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUsername())) {
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }

        comment.remove();
        List<Comment> removableList = comment.findRemovableList();
        commentRepository.deleteAll(removableList);
    }
}
