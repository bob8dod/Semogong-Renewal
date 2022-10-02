package talkwith.semogong.repository.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.dto.member.MemberCreateForm;
import talkwith.semogong.domain.dto.post.PostEditForm;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.repository.member.MemberRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

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
            Post post = Post.create(member);
            postRepository.save(post);
            PostEditForm postEditForm = new PostEditForm();
            postEditForm.setTitle("글" + i);
            postEditForm.setTimes(List.of(now().format(ofPattern("HH:mm"))));
            post.edit(postEditForm);
        }
    }

    @Test
    public void paging() throws Exception{
        //given (주어진 것들을 통해) -> beforeEach
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

    }
}