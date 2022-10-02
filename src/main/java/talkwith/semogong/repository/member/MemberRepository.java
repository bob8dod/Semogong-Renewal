package talkwith.semogong.repository.member;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    Optional<Member> findOneByName(String name);

    List<Member> findAllByName(String name);

    Optional<Member> findOneByLoginId(String loginId);

    List<Member> findAllByDesiredJob(DesiredJob desiredJob);

}
