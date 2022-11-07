package talkwith.semogong.domain.dto.member;

import lombok.Data;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.Image;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.etc.Times;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberHomeDto {

    // member info
    private long id;

    private String loginId;

    private String name;
    private String nickname;
    private DesiredJob desiredJob;

    private StudyState state;

    private Image image;
    private String introduce;

    private Times time;
    private Times totalTime;

    public MemberHomeDto(Member member) {
        this.setId(member.getId());
        this.setLoginId(member.getLoginId());
        this.setName(member.getName());
        this.setNickname(member.getNickname());
        this.setDesiredJob(member.getDesiredJob());
        this.setImage(member.getImage());
        this.setState(member.getState());
    }
}
