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
import talkwith.semogong.repository.member.MemberRepository;
import talkwith.semogong.repository.post.PostRepository;

import java.util.List;

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
    public Post findOne(Long id) {
        return postRepository.findById(id).orElse(Post.noPost());
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public Page<Post> findAllByPage(int page) {
        PageRequest pageRequest = PageCond.getPostPageRequest(page);
        return postRepository.findAll(pageRequest);
    }

    // member 최근 게시글 조회
    @Transactional(readOnly = true)
    public Post findRecentPost(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(Member.noMember());
        return postRepository.findFirstByMemberOrderByCreatedDateDesc(member).orElse(Post.noPost());
    }

    // member의 (최근) 일주일 간의 게시글 조회
/*    @Transactional(readOnly = true)
    public List<Post> findWeekPost() {

    }*/

    // 게시글 수정
    public void edit(Long id, PostEditForm editForm) {
        Post post = postRepository.findById(id).orElse(Post.noPost());
        post.edit(editForm);
    }

    // 게시글 시간 추가
    public void addTime(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(Member.noMember());
        Post post = postRepository.findFirstByMemberOrderByCreatedDateDesc(member).orElse(Post.noPost());
        post.addTime(now());
    }

    // 게시글 공부 상태 변경
    public void editState(Long id, StudyState state) {
        Post post = postRepository.findById(id).orElse(Post.noPost());
        post.editState(state);
    }



}
