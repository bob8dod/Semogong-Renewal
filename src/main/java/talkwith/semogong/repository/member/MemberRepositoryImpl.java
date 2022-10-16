package talkwith.semogong.repository.member;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.entity.Member;

import java.util.List;

import static talkwith.semogong.domain.entity.QFollow.follow;
import static talkwith.semogong.domain.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory qm;

    private final NumberExpression<Integer> rankPath = new CaseBuilder()
            .when(member.state.eq(StudyState.END)).then(3)
            .when(member.state.eq(StudyState.BREAKING)).then(2)
            .when(member.state.eq(StudyState.STUDYING)).then(1)
            .otherwise(4);

    @Override
    public List<Member> findAllWithSorting() {
        return qm.selectFrom(member).orderBy(rankPath.asc()).fetch();
    }

    @Override
    public Slice<Member> findAllFollowing(Member follower, Pageable pageable) {
        List<Member> content = qm.select(follow.followed)
                .from(follow)
                .join(follow.followed, member)
                .where(follow.following.eq(follower))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(rankPath.asc())
                .fetch();

        Long total = qm.select(follow.count())
                .from(follow)
                .where(follow.following.eq(follower))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public Slice<Member> findAllFollowed(Member followed, Pageable pageable) {

        List<Member> content = qm.select(follow.following)
                .from(follow)
                .join(follow.following, member)
                .where(follow.followed.eq(followed))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = qm.select(follow.count())
                .from(follow)
                .where(follow.followed.eq(followed))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);
    }
}
