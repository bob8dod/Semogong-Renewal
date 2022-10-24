package talkwith.semogong.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.config.PageCond;
import talkwith.semogong.domain.att.Goal;
import talkwith.semogong.domain.att.Image;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.member.MemberEditForm;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.etc.SearchCond;
import talkwith.semogong.repository.member.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 저장
    public void save(Member member) {
        /* Spring Security를 통한 암호화 */
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    // 회원 단일 조회
    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findById(id).orElse(Member.noMember());
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public Slice<Member> findAll(int page) {
        PageRequest pageRequest = PageCond.getMemberPageRequest(page);
        return memberRepository.findAllWithSorting(pageRequest);
    }

    // LoginId를 통한 회원 조회
    @Transactional(readOnly = true)
    public Member findByLoginId(String loginId) {
        return memberRepository.findOneByLoginId(loginId).orElse(Member.noMember());
    }

    // 검색 조건을 통한 회원 조회
    @Transactional(readOnly = true)
    public Slice<Member> findBySearch(SearchCond cond, int page) {
        PageRequest pageRequest = PageCond.getMemberPageRequest(page);
        return memberRepository.findAllBySearch(cond, pageRequest);
    }

    // 회원 정보 수정
    public void editMember(Long id, MemberEditForm editForm) {
        Member member = memberRepository.findById(id).orElse(Member.noMember());
        member.edit(editForm); // else : 아무것도 동작하지 않음
    }

    // 회원 이미지 수정
    public void editMemberImg(Long id, Image img) {
        Member member = memberRepository.findById(id).orElse(Member.noMember());
        member.editImage(img); // else : 아무것도 동작하지 않음
    }

    // 회원 목표 수정
    public void editMemberGoal(Long id, Goal goal) {
        Member member = memberRepository.findById(id).orElse(Member.noMember());
        member.editGoal(goal); // else : 아무것도 동작하지 않음
    }

    // 회원 공부상태 수정
    public void changeState(Long id, StudyState state) {
        Member member = memberRepository.findById(id).orElse(Member.noMember());
        member.editState(state); // else : 아무것도 동작하지 않음
    }

}
