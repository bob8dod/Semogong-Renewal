package talkwith.semogong.domain;


import org.junit.jupiter.api.Test;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.Role;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.comment.CommentForm;
import talkwith.semogong.domain.dto.member.MemberCreateForm;
import talkwith.semogong.domain.dto.member.MemberEditForm;
import talkwith.semogong.domain.dto.post.PostEditForm;
import talkwith.semogong.domain.entity.Comment;
import talkwith.semogong.domain.entity.Follow;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

// with no Spring
public class AllEntityTest {

    @Test // Member 생성 및 수정
    public void memberTest() throws Exception {

        /* 생성 */
        //given (주어진 것들을 통해)
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("bob8dod");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        //when (이런 기능을 동작했을 때)
        Member member = Member.create(form);
        //then (이런 결과를 확인할 것)
        assertThat(member.getLoginId()).isEqualTo("bob8dod");
        assertThat(member.getName()).isEqualTo("박승일");
        assertThat(member.getRole()).isEqualTo(Role.USER);

        /* 수정 */
        //given (주어진 것들을 통해)
        MemberEditForm editForm = new MemberEditForm();
        editForm.setName("박승일");
        editForm.setNickname("해돌");
        editForm.setDesiredJob(DesiredJob.Frontend);
        editForm.setIntroduce("안녕하세요");
        editForm.setLinks(List.of("github.com", "github.io"));
        //when (이런 기능을 동작했을 때)
        member.edit(editForm);
        //then (이런 결과를 확인할 것)
        assertThat(member.getLoginId()).isEqualTo("bob8dod");
        assertThat(member.getName()).isEqualTo("박승일");
        assertThat(member.getRole()).isEqualTo(Role.USER);
        assertThat(member.getDesiredJob()).isEqualTo(DesiredJob.Frontend);
        assertThat(member.getNickname()).isEqualTo("해돌");
        assertThat(member.getIntroduce()).isEqualTo("안녕하세요");
        assertThat(member.getLinks()).containsAll(List.of("github.com", "github.io"));
    }

    @Test // Member 팔로우,팔로잉 테스트
    public void followTest() throws Exception {
        //given (주어진 것들을 통해)
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("bob8dod");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);

        MemberCreateForm form1 = new MemberCreateForm();
        form1.setLoginId("potential1205");
        form1.setPassword("1234");
        form1.setName("이재훈");
        form1.setDesiredJob(DesiredJob.Backend);
        Member member2 = Member.create(form1);

        //when (이런 기능을 동작했을 때)
        Follow follow = Follow.create(member, member2);

        //then (이런 결과를 확인할 것)
        assertThat(follow.getFollower().getName()).isEqualTo("박승일"); // 팔로 한 사람
        assertThat(follow.getFollowed().getName()).isEqualTo("이재훈"); // 팔로 당한사람
    }

    @Test // 게시글 생성 및 수정
    public void postTest() throws Exception {
        /* 생성 */
        //given (주어진 것들을 통해)
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("bob8dod");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);
        //when (이런 기능을 동작했을 때)
        Post post = Post.create(member, now());
        //then (이런 결과를 확인할 것)
        assertThat(post.getMember().getName()).isEqualTo("박승일");
        assertThat(post.getTitle()).isEqualTo("제목 없음");
        assertThat(post.getState()).isEqualTo(StudyState.STUDYING);

        /* 수정 */
        //given (주어진 것들을 통해)
        PostEditForm editForm = new PostEditForm();
        editForm.setTitle("세모공 개발");
        editForm.setContent("Test");
        editForm.setIntroduce("재밌다");
        editForm.setTimes(List.of("20:00"));
        //when (이런 기능을 동작했을 때)
        post.edit(editForm);
        //then (이런 결과를 확인할 것)
        assertThat(post.getMember().getName()).isEqualTo("박승일");
        assertThat(post.getTitle()).isEqualTo("세모공 개발");
        assertThat(post.getState()).isEqualTo(StudyState.STUDYING);
        assertThat(post.getHtml()).isEqualTo("<p>Test</p>\n");
        assertThat(post.getTimes()).containsExactly("20:00");
    }

    @Test // 댓글 생성 및 수정
    public void commentTest() throws Exception {

        /* 생성 */
        //given (주어진 것들을 통해)
        MemberCreateForm form = new MemberCreateForm();
        form.setLoginId("bob8dod");
        form.setPassword("1234");
        form.setName("박승일");
        form.setDesiredJob(DesiredJob.Backend);
        Member member = Member.create(form);

        Post post = Post.create(member, now());
        PostEditForm editForm = new PostEditForm();
        editForm.setTitle("세모공 개발");
        editForm.setContent("Test");
        editForm.setIntroduce("재밌다");
        editForm.setTimes(List.of("20:00"));
        post.edit(editForm);

        CommentForm commentForm = new CommentForm();
        commentForm.setContent("안녕하세요!!");
        //when (이런 기능을 동작했을 때)
        Comment comment = Comment.create(commentForm, member, post);
        //then (이런 결과를 확인할 것)
        assertThat(comment.getContent()).isEqualTo("안녕하세요!!");
        assertThat(comment.getMember().getName()).isEqualTo("박승일");
        assertThat(comment.getPost().getTimes()).containsExactly("20:00");

        /* 수정 */
        // given
        CommentForm commentEditForm = new CommentForm();
        commentEditForm.setContent("변경!");
        // when
        comment.edit(commentEditForm);
        // then
        assertThat(comment.getContent()).isEqualTo("변경!");
        assertThat(comment.getMember().getName()).isEqualTo("박승일");
        assertThat(comment.getPost().getTimes()).containsExactly("20:00");
    }


}
