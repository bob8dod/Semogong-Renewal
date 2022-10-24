package talkwith.semogong.repository.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.QMember;
import talkwith.semogong.domain.etc.SearchCond;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
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
    public Slice<Member> findAllWithSorting(Pageable pageable) {
        List<Member> content = qm.selectFrom(member)
                .orderBy(rankPath.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = qm.select(member.count())
                .from(member)
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Slice<Member> findAllFollowing(Member member, Pageable pageable) {
        List<Member> content = qm.select(follow.followed)
                .from(follow)
                .join(follow.followed, QMember.member)
                .where(follow.following.eq(member))
                .orderBy(follow.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = qm.select(follow.count())
                .from(follow)
                .where(follow.following.eq(member))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public Slice<Member> findAllFollowed(Member member, Pageable pageable) {

        List<Member> content = qm.select(follow.following)
                .from(follow)
                .join(follow.following, QMember.member)
                .where(follow.followed.eq(member))
                .orderBy(follow.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = qm.select(follow.count())
                .from(follow)
                .where(follow.followed.eq(member))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Slice<Member> findAllBySearch(SearchCond cond, Pageable pageable) {
        List<Member> content = qm.selectFrom(member)
                .where(nameEq(cond.getName()),
                        jobEq(cond.getDesiredJob()))
                .orderBy(rankPath.asc())
                .fetch();
        Long total = qm.select(member.count())
                .from(member)
                .where(nameEq(cond.getName()),
                        jobEq(cond.getDesiredJob()))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 3. 이름 (name)
     * 해당 이름을 가진 모든 회원
     * */
    private BooleanExpression nameEq(String name) {
        return hasText(name) ? member.name.eq(name) : null;
    }

    /**
     * 4. 희망 직무 (desiredJob)
     * 해당 직무를 가진 모든 회원
     * */
    private BooleanExpression jobEq(DesiredJob desiredJob) {
        return desiredJob != null ? member.desiredJob.eq(desiredJob) : null;
    }


    @Override
    public List<Member> findTop5FollowingBySorting(Member member) {
        return qm.select(follow.followed)
                .from(follow)
                .join(follow.followed, QMember.member)
                .where(follow.following.eq(member))
                .orderBy(rankPath.asc())
                .limit(5)
                .fetch();
    }

}
