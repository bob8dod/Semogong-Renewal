package talkwith.semogong.repository.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.Role;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.member.MemberCreateForm;
import talkwith.semogong.domain.dto.member.MemberEditForm;
import talkwith.semogong.domain.entity.Follow;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.etc.SearchCond;
import talkwith.semogong.repository.FollowRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FollowRepository followRepository;

    @BeforeEach
    public void init() {
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("bob8dod");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);
        member.editRole(Role.ADMIN);
        //when (이런 기능을 동작했을 때)
        memberRepository.save(member);

        MemberCreateForm iterForm = null;
        for (int i = 1; i < 30; i++) {
            iterForm = new MemberCreateForm();
            iterForm.setLoginId("bob"+ i + "dod");
            iterForm.setPassword("12"+ i * 23);
            iterForm.setName("member" + i);
            if (i % 3 == 0) iterForm.setDesiredJob(DesiredJob.Backend);
            else if (i %3 == 1)iterForm.setDesiredJob(DesiredJob.Frontend);
            else iterForm.setDesiredJob(DesiredJob.DataEngineer);
            memberRepository.save(Member.create(iterForm));
        }
    }

    @Test
    public void save() throws Exception{
        //given (주어진 것들을 통해)
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("special");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);
        //when (이런 기능을 동작했을 때)
        memberRepository.save(member);


        //then (이런 결과를 확인할 것)
        em.flush();
        em.clear();
        Optional<Member> findMember = memberRepository.findOneByLoginId("special");
        assertThat(findMember.isPresent()).isTrue();
        assertThat(findMember.get().getName()).isEqualTo("박승일");
    }

    @Test
    public void edit() throws Exception{
        //given (주어진 것들을 통해)

        Optional<Member> optionalMember = memberRepository.findById(1L);
        assertThat(optionalMember.isPresent()).isTrue();
        Member member = optionalMember.get();

        MemberEditForm editForm = new MemberEditForm();
        editForm.setName("박승일");
        editForm.setNickname("해돌");
        editForm.setDesiredJob(DesiredJob.Frontend);
        editForm.setIntroduce("안녕하세요");
        editForm.setLinks(List.of("github.com","github.io"));

        //when (이런 기능을 동작했을 때)
        member.edit(editForm);
        em.flush();
        em.clear();
        Optional<Member> byId = memberRepository.findById(1L);
        //then (이런 결과를 확인할 것)
        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getDesiredJob()).isEqualTo(DesiredJob.Frontend);
    }

    @Test
    public void findMethods() throws Exception{
        //given (주어진 것들을 통해) -> beforeEach
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("park");
        form.setPassword("123s4");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);
        member.editState(StudyState.STUDYING);
        memberRepository.save(member);

        //when (이런 기능을 동작했을 때)
        Optional<Member> member20 = memberRepository.findOneByName("member20");
        List<Member> parkMembers = memberRepository.findAllByName("박승일");
        Optional<Member> bob20dod = memberRepository.findOneByLoginId("bob20dod");
        List<Member> allByDesiredJob = memberRepository.findAllByDesiredJob(DesiredJob.Backend);
        Slice<Member> all = memberRepository.findAllWithSorting(PageRequest.of(0,16));

        //then (이런 결과를 확인할 것)
        assertThat(member20.isPresent()).isTrue();
        assertThat(bob20dod.isPresent()).isTrue();
        assertThat(member20.get().getLoginId()).isEqualTo(bob20dod.get().getLoginId());
        assertThat(parkMembers).extracting("name").containsExactly("박승일", "박승일");
        assertThat(allByDesiredJob.get(3)).extracting("desiredJob").isEqualTo(DesiredJob.Backend);
        assertThat(all.getContent().get(0)).extracting("state").isEqualTo(StudyState.STUDYING);

    }

    @Test // 팔로잉하는 사람 조회
    public void findFollowing() throws Exception{
        //given (주어진 것들을 통해)
        Optional<Member> member1 = memberRepository.findById(1L);
        assertThat(member1.isPresent()).isTrue();
        Optional<Member> member2 = memberRepository.findById(2L);
        assertThat(member2.isPresent()).isTrue();
        Optional<Member> member3 = memberRepository.findById(3L);
        assertThat(member3.isPresent()).isTrue();
        Optional<Member> member4 = memberRepository.findById(4L);
        assertThat(member4.isPresent()).isTrue();

        Follow follow1 = Follow.create(member2.get(), member1.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow2 = Follow.create(member2.get(), member3.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow3 = Follow.create(member2.get(), member4.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람

        followRepository.save(follow1);
        followRepository.save(follow2);
        followRepository.save(follow3);

        em.flush();
        em.clear();

        //when (이런 기능을 동작했을 때)
        Slice<Member> following = memberRepository.findAllFollowing(member2.get(), PageRequest.of(0, 10));

        //then (이런 결과를 확인할 것)
        assertThat(following.getContent().size()).isEqualTo(3);

    }

    @Test
    public void findFollowed() throws Exception{
        //given (주어진 것들을 통해)
        Optional<Member> member1 = memberRepository.findById(1L);
        assertThat(member1.isPresent()).isTrue();
        Optional<Member> member2 = memberRepository.findById(2L);
        assertThat(member2.isPresent()).isTrue();
        Optional<Member> member3 = memberRepository.findById(3L);
        assertThat(member3.isPresent()).isTrue();
        Optional<Member> member4 = memberRepository.findById(4L);
        assertThat(member4.isPresent()).isTrue();

        Follow follow1 = Follow.create(member2.get(), member1.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow2 = Follow.create(member2.get(), member3.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow3 = Follow.create(member3.get(), member1.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow4 = Follow.create(member2.get(), member4.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow5 = Follow.create(member4.get(), member1.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람

        followRepository.save(follow1);
        followRepository.save(follow2);
        followRepository.save(follow3);
        followRepository.save(follow4);
        followRepository.save(follow5);

        em.flush();
        em.clear();

        //when (이런 기능을 동작했을 때)
        Slice<Member> followers = memberRepository.findAllFollowed(member1.get(), PageRequest.of(0, 10));

        //then (이런 결과를 확인할 것)
        assertThat(followers.getContent().size()).isEqualTo(3);
    }

    @Test
    public void searchMemberByName() throws Exception{
        //given (주어진 것들을 통해)

        //when (이런 기능을 동작했을 때)
        SearchCond cond = new SearchCond();
        cond.setName("member11");
        Slice<Member> result = memberRepository.findAllBySearch(cond, PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "createdDate")));
        //then (이런 결과를 확인할 것)
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0)).extracting("loginId").isEqualTo("bob11dod");
    }

    @Test
    public void top5FollowingMember() throws Exception{
        //given (주어진 것들을 통해)
        Optional<Member> member1 = memberRepository.findById(1L);
        assertThat(member1.isPresent()).isTrue();
        Optional<Member> member2 = memberRepository.findById(2L);
        assertThat(member2.isPresent()).isTrue();
        Optional<Member> member3 = memberRepository.findById(3L);
        assertThat(member3.isPresent()).isTrue();
        Optional<Member> member4 = memberRepository.findById(4L);
        assertThat(member4.isPresent()).isTrue();

        Follow follow1 = Follow.create(member2.get(), member1.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow2 = Follow.create(member2.get(), member3.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람
        Follow follow3 = Follow.create(member2.get(), member4.get()); // 팔로 하고자하는 사람, 팔로 당하는 사람

        followRepository.save(follow1);
        followRepository.save(follow2);
        followRepository.save(follow3);

        //when (이런 기능을 동작했을 때)
        List<Member> result = memberRepository.findTop5FollowingBySorting(member2.get());
        //then (이런 결과를 확인할 것)
        assertThat(result.size()).isEqualTo(3);
    }

}