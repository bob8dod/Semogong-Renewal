package talkwith.semogong.domain.att;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("관리자"), USER("사용자");
    private final String describe;
}
