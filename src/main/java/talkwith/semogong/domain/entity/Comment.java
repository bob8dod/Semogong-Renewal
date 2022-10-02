package talkwith.semogong.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import talkwith.semogong.domain.dto.comment.CommentForm;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    // 댓글 생성 메서드
    public static Comment create(CommentForm form, Member member, Post post) {
        Comment comment = new Comment();
        comment.content = form.getContent();
        comment.member = member;
        comment.post = post;
        return comment;
    }

    // 댓글 수정 메서드
    public void edit(CommentForm form) {this.content = form.getContent();}
}
