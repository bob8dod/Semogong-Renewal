package talkwith.semogong.repository.post;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.entity.QMember;
import talkwith.semogong.domain.entity.QPost;
import talkwith.semogong.domain.etc.SearchCond;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static talkwith.semogong.domain.entity.QMember.member;
import static talkwith.semogong.domain.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory qm;

    @Override
    public Page<Post> findBySearch(SearchCond cond, Pageable pageable) {
        List<Post> content = qm.selectFrom(post)
                .join(member)
                .where(titleEq(cond.getTitle()),
                        nameEq(cond.getName()),
                        jobEq(cond.getDesiredJob()),
                        contentEq(cond.getContent()))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = qm.select(post.count())
                .from(post)
                .join(member)
                .where(category(cond.getCategory()),
                        titleEq(cond.getTitle()),
                        nameEq(cond.getName()),
                        jobEq(cond.getDesiredJob()),
                        contentEq(cond.getContent()))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);

    }

    private BooleanExpression category(String category) {
        return null;
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? post.title.like("%"+title+"%") : null;
    }

    private BooleanExpression nameEq(String name) {
        return hasText(name) ? member.name.eq(name) : null;
    }

    private BooleanExpression jobEq(DesiredJob desiredJob) {
        return desiredJob != null ? member.desiredJob.eq(desiredJob) : null;
    }

    private BooleanExpression contentEq(String content) {
        return hasText(content) ? post.content.like("%" + content + "%") : null;
    }
}
