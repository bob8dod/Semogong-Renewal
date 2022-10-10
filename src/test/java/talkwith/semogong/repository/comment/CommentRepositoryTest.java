package talkwith.semogong.repository.comment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.comment.CommentForm;
import talkwith.semogong.domain.dto.member.MemberCreateForm;
import talkwith.semogong.domain.dto.post.PostEditForm;
import talkwith.semogong.domain.entity.Comment;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.repository.member.MemberRepository;
import talkwith.semogong.repository.post.PostRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void basicTest() throws Exception{
        //given (주어진 것들을 통해)
        CommentForm commentForm1 = new CommentForm();
        commentForm1.setContent("안녕하세요 ㅋㅋ");

        CommentForm commentForm2 = new CommentForm();
        commentForm2.setContent("안녕하세요 ㅋㅋ");

        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("park");
        form.setPassword("123s4");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);
        member.editState(StudyState.STUDYING);
        memberRepository.save(member);

        Post post = Post.create(member, now());
        postRepository.save(post);
        PostEditForm postEditForm = new PostEditForm();
        postEditForm.setTitle("글1");
        postEditForm.setContent("안녕하세요~~ 1번째 글입니다!");
        post.edit(postEditForm);

        Comment comment = Comment.create(commentForm1, member, post);
        Comment comment2 = Comment.create(commentForm2, member, post);
        commentRepository.save(comment);
        commentRepository.save(comment2);

        em.flush();
        em.clear();

        //when (이런 기능을 동작했을 때)
        List<Comment> result = commentRepository.findAllByPostOrderByCreatedDateDesc(post);
        //then (이런 결과를 확인할 것)
        for (Comment comments : result) {
            System.out.println("comments = " + comments.getCreatedDate());
        }
        assertThat(result.size()).isEqualTo(2);

    }

}