package talkwith.semogong.repository.member;

import talkwith.semogong.domain.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findAllWithSorting();
}
