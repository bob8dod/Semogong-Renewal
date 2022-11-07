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
import talkwith.semogong.domain.dto.member.MemberHomeDto;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.etc.SearchCond;
import talkwith.semogong.repository.member.FollowRepository;
import talkwith.semogong.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

    // 전체 회원 조회 List
    @Transactional(readOnly = true)
    public List<MemberHomeDto> findAll() {
        List<Member> tempAll = memberRepository.findAll();
        return tempAll.stream().map(MemberHomeDto::new).collect(Collectors.toList());
    }


    // 전체 회원 조회 Paging
    @Transactional(readOnly = true)
    public Slice<Member> findAllByPage(int page) {
        PageRequest pageRequest = PageCond.getMemberPageRequest(page);
        return memberRepository.findAllWithSorting(pageRequest);
    }

    // LoginId를 통한 회원 조회
    @Transactional(readOnly = true)
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findOneByLoginId(loginId);
    }

    // 검색 조건을 통한 회원 조회
    @Transactional(readOnly = true)
    public Slice<Member> findBySearch(SearchCond cond, int page) {
        PageRequest pageRequest = PageCond.getMemberPageRequest(page);
        return memberRepository.findAllBySearch(cond, pageRequest);
    }

    // 현재 Member가 팔로잉 하는 회원 조회
    @Transactional(readOnly = true)
    public Slice<Member> findAllFollowing(Member member, int page) {
        PageRequest pageRequest = PageCond.getMemberPageRequest(page);
        return memberRepository.findAllFollowing(member, pageRequest);
    }

    // 현재 Member를 팔로우 하는 회원 조회
    @Transactional(readOnly = true)
    public Slice<Member> findAllFollowed(Member member, int page) {
        PageRequest pageRequest = PageCond.getMemberPageRequest(page);
        return memberRepository.findAllFollowed(member, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<MemberHomeDto> findTop5Following(Member member) {
        return memberRepository.findTop5FollowingBySorting(member);
    }

    // 회원 정보 수정
    public void editMember(Long id, MemberEditForm editForm) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.edit(editForm);
        } // else : 아무것도 동작하지 않음
    }

    // 회원 이미지 수정
    public void editMemberImg(Long id, Image img) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.editImage(img);
        } // else : 아무것도 동작하지 않음
    }

    // 회원 목표 수정
    public void editMemberGoal(Long id, Goal goal) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.editGoal(goal);
        } // else : 아무것도 동작하지 않음
    }

    // 회원 공부상태 수정
    public void changeState(Long id, StudyState state) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.editState(state);
        } // else : 아무것도 동작하지 않음
    }

}
