package talkwith.semogong.repository.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.member.MemberHomeDto;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.QMember;
import talkwith.semogong.domain.etc.SearchCond;

import java.util.List;

import static com.querydsl.core.types.Projections.constructor;
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
     * 3. ?????? (name)
     * ?????? ????????? ?????? ?????? ??????
     */
    private BooleanExpression nameEq(String name) {
        return hasText(name) ? member.name.eq(name) : null;
    }

    /**
     * 4. ?????? ?????? (desiredJob)
     * ?????? ????????? ?????? ?????? ??????
     */
    private BooleanExpression jobEq(DesiredJob desiredJob) {
        return desiredJob != null ? member.desiredJob.eq(desiredJob) : null;
    }


    @Override
    public List<MemberHomeDto> findTop5FollowingBySorting(Member member) {
        return qm.select(constructor(MemberHomeDto.class, follow.followed))
                .from(follow)
                .join(follow.followed, QMember.member)
                .where(follow.following.eq(member))
                .orderBy(rankPath.asc())
                .limit(5)
                .fetch();
    }

}
