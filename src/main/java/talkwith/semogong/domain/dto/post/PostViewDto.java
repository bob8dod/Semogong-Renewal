package talkwith.semogong.domain.dto.post;

import lombok.Data;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.Image;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.time.format.FormatStyle.LONG;

@Data
public class PostViewDto {

    // Post Info
    private long id;
    private String title;
    private String introduce;
    private String content;
    private String html;
    private LocalDateTime createTime;
    private LocalDate customDate;
    private String formatCreateDate;
    private List<String> times = new ArrayList<>();
    private long commentCount;
    private StudyState state;
    private Image postImg;
    private Image memberImg;

    // Member Info
    private Long memberId;
    private String memberName;
    private String memberNickname;
    private DesiredJob memberDesiredJob;


    public PostViewDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.introduce = post.getIntroduce();
        this.content = post.getContent();
        this.html = post.getHtml();
        this.createTime = post.getCreatedDate();
        this.customDate = post.getCustomDate();
        this.times = post.getTimes();
        this.commentCount = post.getCommentCount();
        this.state = post.getState();
        this.memberName = post.getMember().getName();
        this.memberNickname = post.getMember().getNickname();
        this.memberId = post.getMember().getId();
        this.memberDesiredJob = post.getMember().getDesiredJob();
        this.postImg = post.getImage();
        this.memberImg = post.getMember().getImage();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(LONG).withLocale(Locale.ENGLISH);
        this.formatCreateDate = createTime.format(dateFormatter);
    }
}
