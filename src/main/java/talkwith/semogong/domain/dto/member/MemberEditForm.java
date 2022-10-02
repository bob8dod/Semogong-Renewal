package talkwith.semogong.domain.dto.member;

import lombok.Data;
import talkwith.semogong.domain.att.DesiredJob;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberEditForm {
    @NotBlank
    private String name;
    private String nickname;
    @NotNull
    private DesiredJob desiredJob;
    private String introduce;
    private List<String> links = new ArrayList<>();
}
