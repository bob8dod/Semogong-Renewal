package talkwith.semogong.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.util.StringUtils;
import talkwith.semogong.domain.att.*;
import talkwith.semogong.domain.dto.member.MemberCreateForm;
import talkwith.semogong.domain.dto.member.MemberEditForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private long id;
    private String name;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private DesiredJob desiredJob;
    private String introduce;

    // authorization
    private String loginId;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role; //권한

    //Tier
    private Tier tier;
    private long totalTime;

    // additional information
    @Enumerated(EnumType.STRING)
    private StudyState state;
    @Embedded
    private Image image;
    @Embedded
    private Goal goal;
    @ElementCollection
    private List<String> links = new ArrayList<>();

    // 멤버 생성 메서드
    public static Member create(MemberCreateForm form) {
        Member member = new Member();

        // 입력 값 부여
        member.loginId = form.getLoginId();
        member.password = form.getPassword();
        member.name = form.getName();
        if (hasText(form.getNickname())) member.nickname = form.getNickname();
        else member.nickname = form.getName(); // default
        member.desiredJob = form.getDesiredJob();
        if (hasText(form.getIntroduce())) member.introduce = form.getIntroduce();
        else member.introduce = "안녕하세요, "+member.nickname+"입니다."; // default

        // default 값 부여
        member.goal = new Goal(6, 30);
        member.role = Role.USER;
        member.state = StudyState.END;
        member.image = new Image("Default", "/images/profile.png");
        return member;
    }

    // 존재하지 않는 멤버 반환
    public static Member noMember() {
        return new Member();
    }

    // 멤버 통합 수정 메서드
    public void edit(MemberEditForm form) {
        this.name = form.getName();
        this.desiredJob = form.getDesiredJob();
        if (hasText(form.getNickname())) this.nickname = form.getNickname();
        if (hasText(form.getIntroduce())) this.introduce = form.getIntroduce();
        if (!form.getLinks().isEmpty()) this.links = form.getLinks();
    }

    // 멤버 필드 수정 메서드
    public void editState(StudyState state) {this.state = state;}
    public void editImage(Image image) {this.image = image;}
    public void editGoal(Goal goal) {this.goal = goal;}
    public void editRole(Role role) {this.role = role;}
}

