package talkwith.semogong.domain.dto.member;

import lombok.Data;
import talkwith.semogong.domain.att.DesiredJob;
import talkwith.semogong.domain.att.Image;

@Data
public class MemberBasicDto {
    private long id;
    private String name;
    private String nickname;
    private DesiredJob desiredJob;
    private Image image;
}
