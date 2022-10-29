package talkwith.semogong.repository.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.etc.CustomLocalDate;
import talkwith.semogong.domain.etc.SearchCond;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.springframework.util.StringUtils.hasText;
import static talkwith.semogong.domain.entity.QMember.member;
import static talkwith.semogong.domain.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    static final String MY = "My";
    static final String TODAY = "Today";

    private final JPAQueryFactory qm;

    @Override // 조건에 따른 검색
    public Page<Post> findAllBySearch(SearchCond cond, Member loginMember, Pageable pageable) {
        List<Post> content = qm.selectFrom(post)
                .join(post.member, member)
                .where(category(cond.getCategory(), loginMember),
                        titleEq(cond.getTitle()),
                        nameEq(cond.getName()),
                        jobEq(cond.getDesiredJob()),
                        contentEq(cond.getContent()))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = qm.select(post.count())
                .from(post)
                .join(post.member, member)
                .where(category(cond.getCategory(), loginMember),
                        titleEq(cond.getTitle()),
                        nameEq(cond.getName()),
                        jobEq(cond.getDesiredJob()),
                        contentEq(cond.getContent()))
                .fetchOne();

        if (total == null) return new PageImpl<>(content, pageable, 0);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Post> findAllByCustomDateWithMonth(Member member, int year, int month) {
        return qm.selectFrom(post)
                .where(post.customDate.year().eq(year),
                        post.customDate.month().eq(month),
                        post.member.eq(member))
                .fetch();
    }

    /**
     *  1. 카테고리 (category -> "All", "Today", "My")
     *   1) Today
     *      '오늘'의 기준 (요청 시간에 따라 달라지는 '오늘')
     *          a) 00시 ~ 05시 : 현재 날짜의 이전 날짜의 05시 이후
     *          b) 나머지 : 현재 날짜의 05시 이후
     *   2) My
     *      '나의 글'
     *      로그인된 회원의 모든 글 조회
     *   3) else (All)
     *      '모든 글'
     *      모든 글 조회
    * */
    private BooleanExpression category(String category, Member loginMember) {
        if (!hasText(category)) { // 오류 방지
            return null;
        } else if (category.equals(TODAY)) { // 카테고리 'Today'에서 검색
            LocalDate date = CustomLocalDate.now();
            return post.customDate.eq(date);
        } else if (category.equals(MY)) { // 카테고리 'My'에서 검색
            return post.member.eq(loginMember);
        } else { // All
            return null;
        }
    }

    /**
     * 2. 제목 (title)
     * 해당 제목 키워드를 포함하는 모든 글
     * */
    private BooleanExpression titleEq(String title) {
        return hasText(title) ? post.title.like("%" + title + "%") : null;
    }

    /**
     * 3. 작성자 (name)
     * 해당 작성자의 모든 글
     * */
    private BooleanExpression nameEq(String name) {
        return hasText(name) ? member.name.eq(name) : null;
    }

    /**
     * 4. 희망 직무 (desiredJob)
     * 해당 희망 직무를 가지고 있는 모든 회원의 모든 글
     * */
    private BooleanExpression jobEq(DesiredJob desiredJob) {
        return desiredJob != null ? member.desiredJob.eq(desiredJob) : null;
    }

    /**
     * 5. 내용 (content)
     * 해당 내용 키워드를 포함하는 모든 글
     * */
    private BooleanExpression contentEq(String content) {
        return hasText(content) ? post.content.like("%" + content + "%") : null;
    }


}
