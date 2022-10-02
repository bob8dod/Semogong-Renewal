package talkwith.semogong.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.util.StringUtils;
import talkwith.semogong.domain.att.Image;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.post.PostEditForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.hasText;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "post_id")
    private long id;

    private String title;
    private String introduce;
    @Enumerated(EnumType.STRING)
    private StudyState state;
    @Column(columnDefinition="TEXT")
    private String content;
    @Column(columnDefinition="TEXT")
    private String html;
    @ElementCollection
    private List<String> times = new ArrayList<>();
    @Embedded
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    // 게시글 생성 메서드
    public static Post create(Member member) {
        Post post = new Post();
        post.member = member;
        post.title = "제목 없음";
        post.times.add(now().format(ofPattern("HH:mm")));
        post.state = StudyState.STUDYING;
        return post;
    }

    // 게시글 통합 수정 메서드
    public void edit(PostEditForm form) {
        this.title = form.getTitle();
        this.introduce = form.getIntroduce();
        this.content = form.getContent();
        if (hasText(form.getContent())) this.html = markdownToHTML(form.getContent());
        else this.html = "\n";
        this.times = form.getTimes();
    }

    // 게시글 필드 수정 메서드
    public void editImage(Image image) {this.image = image;}
    public void editState(StudyState state) {this.state = state;}
    public void addTime(String time) {this.times.add(time);}

    private String markdownToHTML(String markdown) {
        Node document = Parser.builder().build().parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
