package talkwith.semogong.repository.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.entity.Follow;
import talkwith.semogong.domain.entity.Member;

import java.util.List;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 현재 로그인된 회원이 조회한 회원을 팔로우 하고 있는지 판단
    Optional<Follow> findIsFollowingByFollowedAndFollowing(Member followed, Member following);

    // 현재 로그인된 회원을 조회한 회원이 팔로우 하고 있는지 판단
    Optional<Follow> findIsFollowedByFollowingAndFollowed(Member following, Member followed);
}
