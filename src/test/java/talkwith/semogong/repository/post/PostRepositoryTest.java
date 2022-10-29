package talkwith.semogong.repository.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.member.MemberCreateForm;
import talkwith.semogong.domain.dto.post.PostEditForm;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.etc.CustomLocalDate;
import talkwith.semogong.domain.etc.SearchCond;
import talkwith.semogong.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("bob8dod");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);
        memberRepository.save(member);

        for (int i = 0; i < 100; i++) {
            Post post = Post.create(member, now());
            postRepository.save(post);
            PostEditForm postEditForm = new PostEditForm();
            postEditForm.setTitle("글" + i);
            postEditForm.setContent("안녕하세요~~ " + i + "번째 글입니다!");
            post.edit(postEditForm);
        }

        em.flush();
        em.clear();
    }

    @Test // 전체 글 조회 with 페이징
    public void basicPaging() throws Exception {
        //given (주어진 것들을 통해) -> beforeEach
        em.flush();
        em.clear();
        //when (이런 기능을 동작했을 때)
        Page<Post> result = postRepository.findAll(PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        //then (이런 결과를 확인할 것)
        long totalElements = result.getTotalElements();
        int totalPages = result.getTotalPages();
        int size = result.getSize();
        List<Post> content = result.getContent();

        assertThat(totalElements).isEqualTo(100);
        assertThat(totalPages).isEqualTo(7);
        assertThat(size).isEqualTo(16);
        assertThat(content.get(0).getTitle()).isEqualTo("글99");
        assertThat(content.get(0).getMember().getName()).isEqualTo("박승일");
    }

    @Test // Member에 따른 글 조회 with 페이징
    public void memberPostByPaging() throws Exception {
        //given (주어진 것들을 통해)
        Optional<Member> member = memberRepository.findOneByName("박승일");
        assertThat(member.isPresent()).isTrue();
        /* em.flush();
        em.clear(); */
        //when (이런 기능을 동작했을 때)
        Page<Post> result = postRepository.findAllByMember(member.get(), PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        // postRepository.findAllByMemberOrderByCreatedDateDesc(member.get(), PageRequest.of(0, 16));
        long totalElements = result.getTotalElements();
        int totalPages = result.getTotalPages();
        int size = result.getSize();
        List<Post> content = result.getContent();

        //then (이런 결과를 확인할 것)
        assertThat(totalElements).isEqualTo(100);
        assertThat(totalPages).isEqualTo(7);
        assertThat(size).isEqualTo(16);
        assertThat(content.get(0).getTitle()).isEqualTo("글99");
        assertThat(content.get(0).getMember().getName()).isEqualTo("박승일");
    }

    @Test // Member 최신 글 단건 조회
    public void memberRecentPost() throws Exception {
        //given (주어진 것들을 통해)
        Optional<Member> member = memberRepository.findOneByName("박승일");
        assertThat(member.isPresent()).isTrue();
        //when (이런 기능을 동작했을 때)
//        Optional<Post> post = postRepository.findFirstByMemberOrderByCreatedDateDesc(member.get());
        Optional<Post> postTemp = postRepository.findFirstByMemberAndStateAndCustomDate(member.get(), StudyState.STUDYING, CustomLocalDate.now());

        //then (이런 결과를 확인할 것)
        assertThat(postTemp.isPresent()).isTrue();
        assertThat(postTemp.get().getTitle()).isEqualTo("글99");
    }

    @Test // 검색 조건에 따른 Paging
    public void postBySearchCondWithPagingAll() throws Exception{
        //given (주어진 것들을 통해)
        SearchCond cond = new SearchCond();
        //when (이런 기능을 동작했을 때)
        Page<Post> posts = postRepository.findAllBySearch(cond, null, PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        //then (이런 결과를 확인할 것)
        assertThat(posts.getTotalElements()).isEqualTo(100);
    }

    @Test // 검색 조건에 따른 Paging
    public void postBySearchCondWithPagingToday() throws Exception{
        //given (주어진 것들을 통해)
        SearchCond cond = new SearchCond();
        cond.setCategory("Today");
        //when (이런 기능을 동작했을 때)
        Page<Post> posts = postRepository.findAllBySearch(cond, null, PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        //then (이런 결과를 확인할 것)
        assertThat(posts.getTotalElements()).isEqualTo(100);
    }

    @Test // 검색 조건에 따른 Paging
    public void postBySearchCondWithPagingMy() throws Exception{

        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("potential1205");
        form.setPassword("1234");
        form.setName("이재훈");
        form.setDesiredJob(DesiredJob.Frontend);
        Member member = Member.create(form);
        memberRepository.save(member);

        for (int i = 0; i < 50; i++) {
            Post post = Post.create(member, now());
            postRepository.save(post);
            PostEditForm postEditForm = new PostEditForm();
            postEditForm.setTitle("글" + i);
            postEditForm.setContent("안녕하세요~~ " + i + "번째 글입니다!");
            post.edit(postEditForm);
        }

        //given (주어진 것들을 통해)
        SearchCond cond = new SearchCond();
        cond.setCategory("My");
        //when (이런 기능을 동작했을 때)
        Page<Post> posts = postRepository.findAllBySearch(cond, member, PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        //then (이런 결과를 확인할 것)
        assertThat(posts.getTotalElements()).isEqualTo(50);
    }

    @Test // 검색 조건에 따른 Paging
    public void postBySearchCondWithPaging() throws Exception{

        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("potential1205");
        form.setPassword("1234");
        form.setName("이재훈");
        form.setDesiredJob(DesiredJob.Frontend);
        Member member = Member.create(form);
        memberRepository.save(member);

        for (int i = 0; i < 40; i++) {
            Post post = Post.create(member, now());
            postRepository.save(post);
            PostEditForm postEditForm = new PostEditForm();
            postEditForm.setTitle("글" + i);
            postEditForm.setContent("안녕하세요~~ " + i + "번째 글입니다!");
            post.edit(postEditForm);
        }
        em.flush();
        em.clear();

        //given (주어진 것들을 통해)
        SearchCond cond = new SearchCond();
        cond.setTitle("글");
        cond.setName("이재훈");
        cond.setDesiredJob(DesiredJob.Frontend);
        cond.setContent("하세요");
        //when (이런 기능을 동작했을 때)
        Page<Post> posts = postRepository.findAllBySearch(cond, null, PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        //then (이런 결과를 확인할 것)
        assertThat(posts.getTotalElements()).isEqualTo(40);
    }

    @Test
    public void postCustomDateTestBetween() throws Exception{
        //given (주어진 것들을 통해)
        Member member = memberRepository.findById(1L).orElse(Member.noMember());
        //when (이런 기능을 동작했을 때)
        List<Post> result = postRepository.findAllByMemberAndCustomDateBetween(member, LocalDate.of(2022, 10, 10), LocalDate.now());
        //then (이런 결과를 확인할 것)
        for (Post post : result) {
            System.out.println("post.getCustomDate() = " + post.getCustomDate());
        }
        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    public void postCustomDateTest() throws Exception{
        //given (주어진 것들을 통해)
        Member member = memberRepository.findById(1L).orElse(Member.noMember());
        //when (이런 기능을 동작했을 때)
        List<Post> result = postRepository.findAllByCustomDateWithMonth(member,2022,10);
        //then (이런 결과를 확인할 것)
        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    public void postCustomDateTestOrderByTotalTime() throws Exception{
        //given (주어진 것들을 통해)

        //when (이런 기능을 동작했을 때)
        List<Post> result = postRepository.findAllByCustomDateOrderByTotalTimeDesc(LocalDate.now());
        //then (이런 결과를 확인할 것)
        assertThat(result.size()).isEqualTo(100);
    }


}