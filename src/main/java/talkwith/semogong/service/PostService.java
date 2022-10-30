package talkwith.semogong.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.config.PageCond;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.post.PostEditForm;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.etc.CustomLocalDate;
import talkwith.semogong.domain.etc.SearchCond;
import talkwith.semogong.repository.member.MemberRepository;
import talkwith.semogong.repository.post.PostRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 게시글 저장
    public Long save(Long memberId) {
        Post post = Post.create(memberRepository.findById(memberId).orElse(Member.noMember()), now());
        postRepository.save(post);
        return post.getId();
    }

    // id를 통한 게시글 단건 조회
    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) {
        return postRepository.findById(id);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public Page<Post> findAllByPage(int page) {
        PageRequest pageRequest = PageCond.getPostPageRequest(page);
        return postRepository.findAll(pageRequest);
    }

    // 전체 member의 오늘 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> findAllToday() {
        return postRepository.findAllByCustomDateOrderByTotalTimeDesc(CustomLocalDate.now());
    }

    // member 최근(오늘) 게시글 조회
    @Transactional(readOnly = true)
    public Optional<Post> findTodayPost(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return postRepository.findFirstByMemberAndCustomDate(member, CustomLocalDate.now());
        } else {
            return Optional.empty();
        }
    }

    // member의 (최근) 일주일 간의 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> findWeekPost(Long memberId, LocalDate from, LocalDate to) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return postRepository.findAllByMemberAndCustomDateBetween(member, from, to);
        } else {
            return new ArrayList<>();
        }
    }

    // member 한달 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> findMonthPost(Long memberId, int year, int month) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return postRepository.findAllByCustomDateWithMonth(member, year, month);
        } else {
            return new ArrayList<>();
        }
    }

    // 검색조건에 따른 게시글 조회
    @Transactional(readOnly = true)
    public Page<Post> findBySearch(SearchCond cond, Member loginMember, int page) {
        return postRepository.findAllBySearch(cond, loginMember, PageCond.getPostPageRequest(page));
    }

    // 게시글 수정
    public void edit(Long id, PostEditForm editForm) {
        Post post = postRepository.findById(id).orElse(Post.noPost());
        post.edit(editForm);
    }

    // 게시글 시간 추가
    public void addTime(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(Member.noMember());
        Post post = postRepository.findFirstByMemberAndCustomDate(member, CustomLocalDate.now()).orElse(Post.noPost());
        post.addTime(now());
    }

    // 게시글 공부 상태 변경
    public void editState(Long id, StudyState state) {
        Post post = postRepository.findById(id).orElse(Post.noPost());
        post.editState(state);
    }

    // 게시글 삭제
    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
