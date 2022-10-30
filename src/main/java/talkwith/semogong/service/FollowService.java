package talkwith.semogong.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.entity.Follow;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.repository.member.FollowRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public void save(Follow follow) {
        followRepository.save(follow);
    }

    // 현재 로그인된 회원이 조회한 회원을 팔로우 하고 있는지 판단
    @Transactional(readOnly = true)
    public Boolean isFollowing(Member following, Member followed) {
        Optional<Follow> optionalFollow = followRepository.findIsFollowingByFollowedAndFollowing(followed, following);
        if (optionalFollow.isPresent()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    // 현재 로그인된 회원을 조회한 회원이 팔로우 하고 있는지 판단
    @Transactional(readOnly = true)
    public Boolean isFollowed(Member followed, Member following) {
        Optional<Follow> optionalFollow = followRepository.findIsFollowedByFollowingAndFollowed(following, followed);
        if (optionalFollow.isPresent()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
