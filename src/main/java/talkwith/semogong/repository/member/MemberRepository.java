package talkwith.semogong.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    // 이름으로 Member 단일 조회
    Optional<Member> findOneByName(String name);

    // 이름으로 Member 다건 조회
    List<Member> findAllByName(String name);

    // 로그인 아이디로 Member 단건 조회
    Optional<Member> findOneByLoginId(String loginId);

    // 희망 직무로 Member 다건 조회
    List<Member> findAllByDesiredJob(DesiredJob desiredJob);


}
