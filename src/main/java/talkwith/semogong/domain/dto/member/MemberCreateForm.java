package talkwith.semogong.domain.dto.member;

import lombok.Data;
import talkwith.semogong.custom.annotation.LoginId;
import talkwith.semogong.custom.annotation.Password;
import talkwith.semogong.domain.att.DesiredJob;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MemberCreateForm {

    @LoginId
    private String loginId;
    @Password
    private String password;
    @NotBlank
    private String name;
    private String nickname;
    @NotNull
    private DesiredJob desiredJob;
    private String introduce;
}
