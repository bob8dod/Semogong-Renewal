package talkwith.semogong.repository.member;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.entity.Member;

import java.util.List;

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
}
