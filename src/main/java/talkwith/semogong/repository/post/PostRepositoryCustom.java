package talkwith.semogong.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.etc.SearchCond;

import java.util.List;

public interface PostRepositoryCustom {

    /**
     * 조건에 따른 검색
     * 조건:
     *  1. 카테고리 (category -> "All", "Today", "My")
     *  2. 제목 (title)
     *  3. 작성자 (name)
     *  4. 희망 직무 (desiredJob)
     *  5. 내용 (content)
     * */
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findBySearch(SearchCond cond, Member loginMember, Pageable pageable);

    /**
     * 지정 달의 글 조회
     * */
    @EntityGraph(attributePaths = {"member"})
    List<Post> findByCustomDateWithMonth(int year, int month);

}
