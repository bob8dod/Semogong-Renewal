package talkwith.semogong.repository.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.etc.SearchCond;

import java.util.List;

public interface MemberRepositoryCustom {

    // 모든 Member 정렬 조회
    Slice<Member> findAllWithSorting(Pageable pageable);

    // 현재 Member가 팔로잉 하는 Member List
    Slice<Member> findAllFollowing(Member member, Pageable pageable);

    // 현재 Member를 팔로잉 하는 Member List
    Slice<Member> findAllFollowed(Member member, Pageable pageable);

    // 현재 Member가 팔로잉 하는 Member List By Sort(by StudyState) and Limit(4) (Not Page)
    List<Member> findTop5FollowingBySorting(Member member);

    // 검색 조건에 따른 Member List
    Slice<Member> findAllBySearch(SearchCond cond, Pageable pageable);
}
