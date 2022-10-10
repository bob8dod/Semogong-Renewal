package talkwith.semogong.repository.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import talkwith.semogong.domain.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findAllWithSorting();

    // 현재 Member가 팔로잉 하는 Member List
    Slice<Member> findAllFollowing(Member follower, Pageable pageable);

    // 현재 Member를 팔로잉 하는 Member List
    Slice<Member> findAllFollower(Member followed, Pageable pageable);
}
