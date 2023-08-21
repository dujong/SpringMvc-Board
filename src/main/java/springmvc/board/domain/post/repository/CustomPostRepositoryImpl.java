package springmvc.board.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import springmvc.board.domain.post.Post;
import springmvc.board.domain.post.cond.PostSearchCondition;

import javax.persistence.EntityManager;

import java.util.List;

import static springmvc.board.domain.member.QMember.member;
import static springmvc.board.domain.post.QPost.post;

public class CustomPostRepositoryImpl implements CustomPostRepository{
    private final JPAQueryFactory query;

    public CustomPostRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Post> search(PostSearchCondition postSearchCondition, Pageable pageable) {
        List<Post> content = query.selectFrom(post)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle())
                )
                .leftJoin(post.writer, member)
                .fetchJoin()
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = query.selectFrom(post)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle())
                );

        return  PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression contentHasStr(String content) {
        return StringUtils.hasLength(content) ? post.content.contains(content) : null;
    }


    private BooleanExpression titleHasStr(String title) {
        return StringUtils.hasLength(title) ? post.title.contains(title) : null;
    }
}
