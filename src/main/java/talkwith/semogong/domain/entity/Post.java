package talkwith.semogong.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import talkwith.semogong.domain.att.Image;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.post.PostEditForm;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    // 00시~05시 사이에 작성된 글에 대해 처리하기 위한 Field
    private LocalDate customDate;

    // 게시글 생성 메서드
    public static Post create(Member member, LocalDateTime createdTime) {
        Post post = new Post();
        post.member = member;
        // default setting
        post.title = "제목 없음";
        post.introduce = "";
        post.content = "";
        post.html = "<br>";
        post.times.add(createdTime.format(ofPattern("HH:mm")));
        LocalDate date = createdTime.toLocalDate();
        // 00 ~ 05 시 사이 글 작성 시 customDate를 이전 날짜로 설정
        if (createdTime.getHour() < 5) date = createdTime.minusDays(1L).toLocalDate();
        post.customDate = date;
        post.state = StudyState.STUDYING;
        post.image = new Image("Default", "/images/semogong_light.jpg");
        return post;
    }

    // 존재하지 않는 게시글 반환
    public static Post noPost() {
        return new Post();
    }

    // 게시글 통합 수정 메서드
    public void edit(PostEditForm form) {
        this.title = form.getTitle();
        if (hasText(form.getIntroduce())) this.introduce = form.getIntroduce();
        if (hasText(form.getContent())){
            this.content = form.getContent();
            this.html = markdownToHTML(form.getContent());
        }

        this.times = form.getTimes();
    }

    // 게시글 필드 수정 메서드
    public void editImage(Image image) {this.image = image;}
    public void editState(StudyState state) {this.state = state;}
    public void addTime(LocalDateTime time) {
        this.times.add(time.format(ofPattern("HH:mm")));
    }

    private String markdownToHTML(String markdown) {
        Node document = Parser.builder().build().parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
